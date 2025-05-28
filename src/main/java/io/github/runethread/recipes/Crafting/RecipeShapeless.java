package io.github.runethread.recipes.Crafting;

import io.github.runethread.recipes.RecipeResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class RecipeShapeless extends Recipe{
    public RecipeShapeless(List<Ingredient> ingredients, List<RecipeResult> results, int width, int height) {
        super(ingredients, results, width, height);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int inputWidth = input.width();
        int inputHeight = input.height();

        if (inputWidth < width || inputHeight < height) return false;

        List<ItemStack> inputStacks = input.items();

        for (Ingredient ingredient : ingredients) {
            boolean found = false;
            for (ItemStack stack : inputStacks) {
                if (ingredient.test(stack)) {
                    found = true;
                    inputStacks.remove(stack);
                    break;
                }
            }
            if (!found) return false; // If any ingredient is missing, recipe doesn't match
        }
        return true;
    }
}
