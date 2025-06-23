package io.github.runethread.customblocks.craftingtable;

import io.github.runethread.recipes.Crafting.RecipeShaped;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class CraftingEntity extends BlockEntity{
    protected final ItemStackHandler output = new ItemStackHandler(1);
    protected final int width, height;
    protected ItemStackHandler inventory;
    private boolean needsCraftingUpdate = false;

    public CraftingEntity(BlockEntityType<?> blockEntity, BlockPos pos, BlockState state, int width, int height) {
        super(blockEntity, pos, state);
        this.width = width;
        this.height = height;
        inventory = new ItemStackHandler(width * height);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ItemStackHandler getOutput() {
        return output;
    }

    public void tryCraft(Level level) {
        List<ItemStack> items = getCraftingItems();
        CraftingInput input = getRecipeInput(items);

        Optional<RecipeHolder<CraftingRecipe>> recipeOpt = getRecipeOpt(level, input);

        if (recipeOpt.isPresent()) {
            CraftingRecipe recipe = recipeOpt.get().value();
            ItemStack result = recipe.assemble(input, level.registryAccess());
            output.setStackInSlot(0, result.copy());
        } else {
            output.setStackInSlot(0, ItemStack.EMPTY);
        }
    }

    protected abstract Optional<RecipeHolder<CraftingRecipe>> getRecipeOpt(Level level, CraftingInput input);

    protected CraftingInput getRecipeInput(List<ItemStack> items) {
        return CraftingInput.of(width, height, items);
    }

    protected List<ItemStack> getCraftingItems() {
        List<ItemStack> items = new ArrayList<>(width * height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int idx = row * width + col;
                items.add(inventory.getStackInSlot(idx));
            }
        }
        return items;
    }

    public void doCraft(Level level) {
        if (level.isClientSide) return;
        for (int i = 0; i < width * height; i++) {
            removeItem(i, inventory);
        }
        output.setStackInSlot(0, ItemStack.EMPTY);
        tryCraft(level);
    }

    public void doCraftShift(Level level, Player player) {
        ItemStack preview = output.getStackInSlot(0);
        if (preview.isEmpty()) return;

        List<ItemStack> items;

        while (true) {
            items = getCraftingItems();
            CraftingInput input = getRecipeInput(items);
            Optional<RecipeHolder<CraftingRecipe>> recipeOpt = getRecipeOpt(level, input);
            if (recipeOpt.isEmpty()) break;

            RecipeShaped recipe = (RecipeShaped) recipeOpt.get().value();

            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                removeItem(i, inventory);
            }
            player.addItem(recipe.assemble(input, level.registryAccess()));
        }

        tryCraft(level);
    }

    private void removeItem(int index, ItemStackHandler inventory){
        ItemStack item = inventory.extractItem(index, 1, false);
        ItemStack remainder = item.getCraftingRemainder();
        if (!remainder.isEmpty()) {
            inventory.insertItem(index, remainder, false);
        }
    }

    public void tick() {
        if (needsCraftingUpdate) {
            needsCraftingUpdate = false;
            tryCraft(level);
            this.setChanged();
        }
    }

    public void scheduleCraftingUpdate() {
        needsCraftingUpdate = true;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.put("Output", output.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Inventory", Tag.TAG_COMPOUND)) {
            inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        }
        if (tag.contains("Output", Tag.TAG_COMPOUND)) {
            output.deserializeNBT(registries, tag.getCompound("Output"));
        }
    }
}
