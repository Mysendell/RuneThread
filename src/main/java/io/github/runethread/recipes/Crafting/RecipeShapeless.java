package io.github.runethread.recipes.Crafting;

import io.github.runethread.recipes.CustomRecipes;
import io.github.runethread.recipes.IRecipeIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A shapeless crafting recipe. The ingredients can be in any order in the crafting grid.
 * @see RecipeShaped
 */
public class RecipeShapeless extends ModRecipe {
    public RecipeShapeless(List<IRecipeIngredient> ingredients, List<IRecipeIngredient> results, int recipeWidth, int recipeHeight) {
        super(ingredients, results, recipeWidth, recipeHeight);
    }

    /**
     * Checks if the given crafting input matches this recipe.
     * @param input the crafting input to check
     * @param level the level the crafting is taking place in
     * @return true if the input matches this recipe, false otherwise
     */
    @Override
    public boolean matches(CraftingInput input, @NotNull Level level) {
        int inputWidth = input.width();
        int inputHeight = input.height();

        if (inputWidth < recipeWidth || inputHeight < recipeHeight) return false;

        List<ItemStack> inputStacks = input.items();

        for (IRecipeIngredient expected : ingredients) { // TODO possible optimization
            boolean found = false;
            for (ItemStack stack : inputStacks) {
                if (expected.test(stack)) {
                    found = true;
                    inputStacks.remove(stack);
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return CustomRecipes.RECIPE_SHAPELESS_SERIALIZER.get();
    }

    @Override
    public @NotNull CraftingBookCategory category() {
        return null;
    }

    @Override
    public @NotNull RecipeType<CraftingRecipe> getType() {
        return CustomRecipes.RECIPE_SHAPELESS.get();
    }
}
