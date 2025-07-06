package io.github.runethread.customblocks.craftingtable;

import io.github.runethread.recipes.smelting.Smelting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;
import java.util.Optional;

import static io.github.runethread.util.InventoryUtil.*;

public abstract class FurnaceEntity<T extends Smelting, J extends FurnaceBlock> extends BlockEntity implements MenuProvider {
    protected int inputWidth = 1, inputHeight = 1;
    protected int outputWidth = 1, outputHeight = 1;
    protected int fuelWidth = 1, fuelHeight = 1;
    protected ItemStackHandler input = new ItemStackHandler(inputWidth * inputHeight);
    protected ItemStackHandler output = new ItemStackHandler(outputWidth * outputHeight);
    protected ItemStackHandler fuel = new ItemStackHandler(fuelWidth * fuelHeight);
    protected int fuelBurnTime = 0, burnTime = 0, recipeBurnTime = 0;
    protected int furnaceSpeed = 200;
    protected HolderLookup.Provider registries;
    protected FeatureFlagSet enabledFeatures;
    protected FuelValues fuelValues;
    protected boolean needsUpdate = true;
    protected int fuelBurnMultiplier = 1;
    protected T recipe;
    protected ItemStack result;


    public FurnaceEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public Optional<RecipeHolder<CraftingRecipe>> canCraft(Level level) {

        List<ItemStack> inputItems = getCraftingItems(inputWidth, inputHeight, input);
        CraftingInput inputRecipe = getRecipeInput(inputWidth, inputHeight, inputItems);

        Optional<RecipeHolder<CraftingRecipe>> recipeOpt = getRecipeOpt(level, inputRecipe);

        setLitState(recipeOpt.isPresent());

        return recipeOpt;
    }

    private void setLitState(boolean lit) {
        if (level != null && !level.isClientSide) {
            BlockState state = level.getBlockState(worldPosition);
            if (state.hasProperty(J.LIT) && state.getValue(J.LIT) != lit) {
                level.setBlock(worldPosition, state.setValue(J.LIT, lit), 3);
            }
        }
    }

    protected void serverTick(Level level) {
        if (registries == null) {
            registries = level.registryAccess();
            enabledFeatures = level.enabledFeatures();
            fuelValues = FuelValues.vanillaBurnTimes(registries, enabledFeatures);
        }

        if (needsUpdate) {
            needsUpdate = false;
            this.setChanged();
            Optional<RecipeHolder<CraftingRecipe>> recipeOpt = canCraft(level);
            recipe = (T) recipeOpt.map(RecipeHolder::value).orElse(null);
            if (recipe != null) {
                List<ItemStack> inputItems = getCraftingItems(inputWidth, inputHeight, input);
                CraftingInput inputRecipe = getRecipeInput(inputWidth, inputHeight, inputItems);
                result = recipe.assemble(inputRecipe, level.registryAccess());
                recipeBurnTime = recipe.getBurnTime();
                fuelBurnMultiplier = recipe.getFuelBurnMultiplier();
            }
        }

        if(recipe == null) {
            burnTime = 0;
            return;
        }

        if (burnTime >= (recipeBurnTime + furnaceSpeed)) {
            List<ItemStack> outputStacks = getCraftingItems(outputWidth, outputHeight, output);
            if (!canFullyOutput(outputStacks, result)) return;
            burnTime = 0;
            distributeOutput(output, result);
            removeCraftingItems(inputWidth, inputHeight, input);
            needsUpdate = true;
        } else {
            if (fuelBurnTime == 0 && !updateFuel())
                return;
            burnTime++;
            fuelBurnTime -= fuelBurnMultiplier;
        }
    }

    protected boolean updateFuel() {
        List<ItemStack> fuelItems = getCraftingItems(fuelWidth, fuelHeight, fuel);
        for (int i = 0; i < fuelItems.size(); i++) {
            if (fuelItems.get(i).isEmpty()) continue;
            fuelBurnTime = fuelItems.get(i).getBurnTime(getRecipeType(), fuelValues);
            if (fuelBurnTime > 0) {
                fuel.extractItem(i, 1, false);
                break;
            }
        }
        return fuelBurnTime != 0;
    }

    protected abstract RecipeType<?> getRecipeType();

    protected abstract Optional<RecipeHolder<CraftingRecipe>> getRecipeOpt(Level level, CraftingInput input);

    public void onRemove(){
        dropStackHandler(worldPosition, level, fuel);
        dropStackHandler(worldPosition, level, output);
        dropStackHandler(worldPosition, level, input);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries); // Always call super
        tag.put("Inventory", input.serializeNBT(registries));
        tag.putInt("BurnTime", burnTime);
        tag.putInt("FuelBurnTime", fuelBurnTime);
        tag.put("Fuel", fuel.serializeNBT(registries));
        tag.put("Output", output.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries); // Always call super
        if (tag.contains("Inventory", Tag.TAG_COMPOUND)) {
            input.deserializeNBT(registries, tag.getCompound("Inventory"));
        }
        burnTime = tag.getInt("BurnTime");
        fuelBurnTime = tag.getInt("FuelBurnTime");
        if (tag.contains("Fuel", Tag.TAG_COMPOUND)) {
            fuel.deserializeNBT(registries, tag.getCompound("Fuel"));
        }
        if (tag.contains("Output", Tag.TAG_COMPOUND)) {
            output.deserializeNBT(registries, tag.getCompound("Output"));
        }
    }

    public void scheduleCraftingUpdate() {
        needsUpdate = true;
    }

    public boolean isNeedsUpdate() {
        return needsUpdate;
    }

    public void setNeedsUpdate(boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }

    public ItemStackHandler getInput() {
        return input;
    }

    public ItemStackHandler getOutput() {
        return output;
    }

    public ItemStackHandler getFuel() {
        return fuel;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public int getMaxBurnTime() {
        return recipeBurnTime + furnaceSpeed;
    }

    public int getFuelBurnTime() {
        return fuelBurnTime;
    }
}

