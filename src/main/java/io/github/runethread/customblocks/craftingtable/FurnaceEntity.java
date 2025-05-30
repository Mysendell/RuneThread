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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class FurnaceEntity<T extends Smelting, J extends FurnaceBlock> extends BlockEntity implements MenuProvider {
    protected int inputWidth = 1, inputHeight = 1;
    protected int outputWidth = 1, outputHeight = 1;
    protected int fuelWidth = 1, fuelHeight = 1;
    protected ItemStackHandler input = new ItemStackHandler(inputWidth * inputHeight) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            if (!level.isClientSide()) {
                isSmelting2 = false;
                FurnaceEntity.this.setChanged();
            }
        }
    };
    protected ItemStackHandler output = new ItemStackHandler(outputWidth * outputHeight) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            if (!level.isClientSide()) {
                FurnaceEntity.this.setChanged();
            }
        }
    };
    protected ItemStackHandler fuel = new ItemStackHandler(fuelWidth * fuelHeight) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            if (!level.isClientSide()) {
                FurnaceEntity.this.setChanged();
            }
        }
    };
    protected int fuelBurnTime = 0, burnTime = 0, recipeBurnTime = 0;
    protected int furnaceSpeed = 200;
    protected HolderLookup.Provider registries;
    protected FeatureFlagSet enabledFeatures;
    protected FuelValues fuelValues;
    protected boolean isSmelting1 = false, isSmelting2 = false;


    public FurnaceEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
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

    public boolean canCraft(Level level) {
        if (isSmelting2 != isSmelting1)
            return false;

        List<ItemStack> inputItems = getCraftingItems(inputWidth, inputHeight, input);
        CraftingInput inputRecipe = getRecipeInput(inputWidth, inputHeight, inputItems);

        Optional<RecipeHolder<CraftingRecipe>> recipeOpt = getRecipeOpt(level, inputRecipe);

        if (recipeOpt.isPresent()) {
            isSmelting1 = true;
            isSmelting2 = true;
            return true;
        }
        return false;
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

        if (!canCraft(level)) {
            burnTime = 0;
            isSmelting1 = false;
            isSmelting2 = false;
        }
        List<ItemStack> inputItems = getCraftingItems(inputWidth, inputHeight, input);
        List<ItemStack> fuelItems = getCraftingItems(fuelWidth, fuelHeight, fuel);
        List<ItemStack> outputItems = getCraftingItems(outputWidth, outputHeight, output);
        CraftingInput inputRecipe = getRecipeInput(inputWidth, inputHeight, inputItems);

        Optional<RecipeHolder<CraftingRecipe>> recipeOpt = getRecipeOpt(level, inputRecipe);

        if (recipeOpt.isEmpty()) {
            isSmelting1 = false;
            isSmelting2 = false;
            setLitState(false);
            return;
        }

        T recipe = (T) recipeOpt.get().value();
        ItemStack result = recipe.assemble(inputRecipe, level.registryAccess());


        if (!canFullyOutput(outputItems, result)) {
            isSmelting1 = false;
            isSmelting2 = false;
        }

        setLitState(isSmelting1 && isSmelting2);

        if (!isSmelting1 && !isSmelting2) {
            burnTime = 0;
            return;
        }

        recipeBurnTime = recipe.getBurnTime();
        int fuelBurnMultiplier = recipe.getFuelBurnMultiplier();

        if (fuelBurnTime == 0) {
            for (int i = 0; i < fuelItems.size(); i++) {
                if (fuelItems.get(i).isEmpty()) continue;
                fuelBurnTime = fuelItems.get(i).getBurnTime(getRecipeType(), fuelValues);
                if (fuelBurnTime > 0) {
                    fuel.extractItem(i, 1, false);
                    break;
                }
            }
            if (fuelBurnTime == 0) {
                isSmelting1 = false;
                isSmelting2 = false;
                return;
            }
        }
        if (burnTime >= (recipeBurnTime + furnaceSpeed)) {
            burnTime = 0;
            isSmelting1 = false;
            isSmelting2 = false;
            distributeOutput(output, result);
            removeCraftingItems(inputWidth, inputHeight, input);
        } else {
            burnTime++;
            fuelBurnTime -= fuelBurnMultiplier;
        }
    }

    public static boolean canFullyOutput(List<ItemStack> outputItems, ItemStack result) {
        int remaining = result.getCount();

        for (ItemStack outputStack : outputItems) {
            if (remaining <= 0) break;

            if (outputStack.isEmpty()) {
                // Can insert up to the max stack size
                int max = result.getMaxStackSize();
                int canInsert = Math.min(max, remaining);
                remaining -= canInsert;
            } else if (ItemStack.isSameItemSameComponents(outputStack, result)) {
                // Can fill up to max stack size
                int space = outputStack.getMaxStackSize() - outputStack.getCount();
                int canInsert = Math.min(space, remaining);
                remaining -= canInsert;
            }
        }
        return remaining <= 0;
    }

    public static void distributeOutput(ItemStackHandler output, ItemStack result) {
        int toInsert = result.getCount();
        ItemStack toDistribute = result.copy();

        for (int i = 0; i < output.getSlots() && toInsert > 0; i++) {
            // Prepare stack to try to insert
            ItemStack attempt = toDistribute.copy();
            attempt.setCount(toInsert);

            // Try to insert in slot i
            ItemStack remainder = output.insertItem(i, attempt, false); // simulate=false, actually insert

            // Update how much is left to insert
            int inserted = attempt.getCount() - (remainder.isEmpty() ? 0 : remainder.getCount());
            toInsert -= inserted;
        }
    }

    protected void removeCraftingItems(int width, int height, ItemStackHandler inventory) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int idx = row * width + col;
                ItemStack item = inventory.extractItem(idx, 1, false);
                ItemStack remainder = item.getCraftingRemainder();
                if (!remainder.isEmpty()) {
                    inventory.insertItem(idx, remainder, false);
                }
            }
        }
    }

    protected abstract RecipeType<?> getRecipeType();

    protected abstract Optional<RecipeHolder<CraftingRecipe>> getRecipeOpt(Level level, CraftingInput input);

    protected List<ItemStack> getCraftingItems(int width, int height, ItemStackHandler inventory) {
        List<ItemStack> items = new ArrayList<>(width * height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int idx = row * width + col;
                items.add(inventory.getStackInSlot(idx));
            }
        }
        return items;
    }

    protected CraftingInput getRecipeInput(int width, int height, List<ItemStack> items) {
        return CraftingInput.of(width, height, items);
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
}

