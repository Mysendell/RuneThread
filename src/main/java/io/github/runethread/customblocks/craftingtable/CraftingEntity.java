package io.github.runethread.customblocks.craftingtable;

import io.github.runethread.recipes.Recipe;
import io.github.runethread.recipes.RecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class CraftingEntity<T extends RecipeInput, J extends Recipe<T>> extends BlockEntity implements MenuProvider {
    protected final ItemStackHandler inventory = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            if (!level.isClientSide()) {
                tryCraft(level);
            }
        }
    };
    protected final ItemStackHandler output = new ItemStackHandler(1);
    protected final int width = 3, height = 3;

    public CraftingEntity(BlockEntityType<?> blockEntity, BlockPos pos, BlockState state) {
        super(blockEntity, pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ItemStackHandler getOutput() {
        return output;
    }

    public void tryCraft(Level level) {
        List<ItemStack> items = getCraftingItems();
        T input = (T) getRecipeInput(items);

        Optional<RecipeHolder<J>> recipeOpt = getRecipeOpt(level, input);

        if (recipeOpt.isPresent()) {
            J recipe = recipeOpt.get().value();
            ItemStack result = recipe.assemble(input, level.registryAccess());
            output.setStackInSlot(0, result.copy());
        } else {
            output.setStackInSlot(0, ItemStack.EMPTY);
        }
    }

    protected abstract Optional<RecipeHolder<J>> getRecipeOpt(Level level, T input);

    protected abstract T getRecipeInput(List<ItemStack> items);

    protected List<ItemStack> getCraftingItems() {
        List<ItemStack> items = new ArrayList<>(9);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int idx = col * height + row;
                items.add(inventory.getStackInSlot(idx));
            }
        }
        return items;
    }

    public boolean doCraft(Level level) {
        if (level.isClientSide) return false;
        for (int i = 0; i < width * height; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            stack.shrink(1);
            inventory.setStackInSlot(i, stack);
        }
        output.setStackInSlot(0, ItemStack.EMPTY);
        tryCraft(level);
        return true;
    }

    public int doCraftShift(Level level, Player player) {
        ItemStack preview = output.getStackInSlot(0);
        if (preview.isEmpty()) return 0;

        int maxCrafts = Integer.MAX_VALUE;
        List<ItemStack> items;

        while (true) {
            items = new ArrayList<>(9);
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    int idx = col * height + row;
                    items.add(inventory.getStackInSlot(idx));
                }
            }
            T input = (T) getRecipeInput(items);
            Optional<RecipeHolder<J>> recipeOpt = getRecipeOpt(level, input);
            if (recipeOpt.isEmpty()) break;

            J recipe = recipeOpt.get().value();
            boolean canCraft = true;
            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                Ingredient ing = recipe.getIngredients().get(i);
                ItemStack stack = inventory.getStackInSlot(i);
                if (!ing.isEmpty() && (!ing.test(stack) || stack.getCount() < 1)) {
                    canCraft = false;
                    break;
                }
            }
            if (!canCraft) break;

            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                Ingredient ing = recipe.getIngredients().get(i);
                ItemStack stack = inventory.getStackInSlot(i);
                if (!ing.isEmpty() && ing.test(stack) && !stack.isEmpty()) {
                    stack.shrink(1);
                    inventory.setStackInSlot(i, stack);
                }
            }
            player.addItem(recipe.assemble(input, level.registryAccess()));
            maxCrafts--;
            if (maxCrafts <= 0) break;
        }

        tryCraft(level);

        return Integer.MAX_VALUE - maxCrafts;
    }
}
