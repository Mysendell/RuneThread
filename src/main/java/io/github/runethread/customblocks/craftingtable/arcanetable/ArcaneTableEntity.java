package io.github.runethread.customblocks.craftingtable.arcanetable;

import io.github.runethread.menus.ArcaneMenu;
import io.github.runethread.recipes.ArcaneRecipeShaped;
import io.github.runethread.recipes.ArcaneRecipeShapedInput;
import io.github.runethread.recipes.CustomRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.github.runethread.customblocks.CustomBlockEntities.ARCANE_TABLE;

public class ArcaneTableEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler inventory = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            if (!level.isClientSide()) {
                tryCraft(level);
            }
        }
    };
    private final ItemStackHandler output = new ItemStackHandler(1);
    private final int width = 3, height = 3;

    public ArcaneTableEntity(BlockPos pos, BlockState state) {
        super(ARCANE_TABLE.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ItemStackHandler getOutput() {
        return output;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.arcane_table");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ArcaneMenu(containerId, playerInventory, this);
    }

/*
    public void tryCraft(Level level) {
        List<ItemStack> items = new ArrayList<>(9);
        int width = 3, height = 3;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                // Column-major index
                int idx = col * height + row;
                items.add(inventory.getStackInSlot(idx));
            }
        }
        ArcaneRecipeShapedInput input = new ArcaneRecipeShapedInput(items);

        Optional<RecipeHolder<ArcaneRecipeShaped>> recipeOpt = level.getServer().getRecipeManager()
                .getRecipeFor(CustomRecipes.ARCANE_SHAPED.get(), input, level);


        ItemStack currentOutput = output.getStackInSlot(0);

        if (recipeOpt.isPresent()) {
            ArcaneRecipeShaped recipe = recipeOpt.get().value();
            ItemStack result = recipe.assemble(input, level.registryAccess());
            if (currentOutput.isEmpty()) {
                output.setStackInSlot(0, result.copy());
            } else if (ItemStack.isSameItem(currentOutput, result)
                    && currentOutput.getCount() + result.getCount() <= currentOutput.getMaxStackSize()) {
                ItemStack newStack = currentOutput.copy();
                newStack.grow(result.getCount());
                output.setStackInSlot(0, newStack);
            }
            List<Ingredient> ingredients = recipe.getIngredients();
            System.out.println("Ingredients: " + ingredients.size());
            for (int i = 0; i < ingredients.size(); i++) {
                Ingredient ing = ingredients.get(i);
                ItemStack stack = inventory.getStackInSlot(i);
                if (!ing.isEmpty() && ing.test(stack) && !stack.isEmpty()) {
                    stack.shrink(1);
                    inventory.setStackInSlot(i, stack);
                }
            }
        }
    }
*/

    public void tryCraft(Level level) {
        List<ItemStack> items = new ArrayList<>(9);
        int width = 3, height = 3;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int idx = col * height + row;
                items.add(inventory.getStackInSlot(idx));
            }
        }
        ArcaneRecipeShapedInput input = new ArcaneRecipeShapedInput(items);

        Optional<RecipeHolder<ArcaneRecipeShaped>> recipeOpt = level.getServer().getRecipeManager()
                .getRecipeFor(CustomRecipes.ARCANE_SHAPED.get(), input, level);

        if (recipeOpt.isPresent()) {
            ArcaneRecipeShaped recipe = recipeOpt.get().value();
            ItemStack result = recipe.assemble(input, level.registryAccess());
            output.setStackInSlot(0, result.copy());
        } else {
            output.setStackInSlot(0, ItemStack.EMPTY);
        }
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
            ArcaneRecipeShapedInput input = new ArcaneRecipeShapedInput(items);
            Optional<RecipeHolder<ArcaneRecipeShaped>> recipeOpt = level.getServer().getRecipeManager()
                    .getRecipeFor(CustomRecipes.ARCANE_SHAPED.get(), input, level);
            if (recipeOpt.isEmpty()) break;

            ArcaneRecipeShaped recipe = recipeOpt.get().value();
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
