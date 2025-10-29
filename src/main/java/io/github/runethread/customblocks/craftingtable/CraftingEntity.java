package io.github.runethread.customblocks.craftingtable;

import io.github.runethread.recipes.Crafting.ModRecipe;
import io.github.runethread.util.InventoryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;
import java.util.Optional;

import static io.github.runethread.util.InventoryUtil.*;

public abstract class CraftingEntity extends BlockEntity implements ICraftingEntity {
    protected final ItemStackHandler output = new ItemStackHandler(1);
    protected final int width, height;
    protected ItemStackHandler input;
    protected ModRecipe recipe;

    public CraftingEntity(BlockEntityType<?> blockEntity, BlockPos pos, BlockState state, int width, int height) {
        super(blockEntity, pos, state);
        this.width = width;
        this.height = height;
        input = new ItemStackHandler(width * height);
    }

    protected void tryCraft(Level level) {
        List<ItemStack> items = getCraftingItems(width, height, input);
        CraftingInput craftingInput = getRecipeInput(width, height, items);

        Optional<RecipeHolder<CraftingRecipe>> recipeOpt = getRecipeOpt(level, craftingInput);

        if (recipeOpt.isPresent()) {
            recipe = (ModRecipe) recipeOpt.get().value();
            ItemStack result = recipe.assemble(craftingInput, level.registryAccess());
            output.setStackInSlot(0, result.copy());
        } else {
            recipe = null;
            output.setStackInSlot(0, ItemStack.EMPTY);
        }
    }

    protected abstract Optional<RecipeHolder<CraftingRecipe>> getRecipeOpt(Level level, CraftingInput input);

    public void doCraft(Level level) {
        if (level.isClientSide) return;
        int[] countMap = recipe.getIngredientCountMap();
        removeCraftingItems(width, height, input, countMap, worldPosition, level);
        output.setStackInSlot(0, ItemStack.EMPTY);
        tryCraft(level);
    }

    public void doCraftShift(Level level, Player player) {
        ItemStack preview = output.getStackInSlot(0);
        if (preview.isEmpty()) return;

        while (true) {
            List<ItemStack> items = getCraftingItems(width, height, input);
            CraftingInput craftingInput = getRecipeInput(width, height, items);
            Optional<RecipeHolder<CraftingRecipe>> recipeOpt = getRecipeOpt(level, craftingInput);
            if (recipeOpt.isEmpty()) break;

            recipe = (ModRecipe) recipeOpt.get().value();

            int[] countMap = recipe.getIngredientCountMap();
            removeCraftingItems(width, height, input, countMap, worldPosition, level);
            player.addItem(recipe.assemble(craftingInput, level.registryAccess()));
        }

        tryCraft(level);
    }

    public void onRemove(){
        InventoryUtil.dropStackHandler(worldPosition, level, input);
    }

    public void scheduleCraftingUpdate() {
        tryCraft(level);
        this.setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", input.serializeNBT(registries));
        tag.put("Output", output.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Inventory", Tag.TAG_COMPOUND)) {
            input.deserializeNBT(registries, tag.getCompound("Inventory"));
        }
        if (tag.contains("Output", Tag.TAG_COMPOUND)) {
            output.deserializeNBT(registries, tag.getCompound("Output"));
        }
    }

    public ItemStackHandler getInput() {
        return input;
    }

    public ItemStackHandler getOutput() {
        return output;
    }
}
