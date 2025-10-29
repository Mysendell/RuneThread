package io.github.runethread.recipes.Crafting;

import io.github.runethread.recipes.CustomRecipes;
import io.github.runethread.recipes.IRecipeIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A shaped crafting recipe. The arrangement of ingredients matters.
 * @see ModRecipe
 */
public class RecipeShaped extends ModRecipe {


    public RecipeShaped(List<IRecipeIngredient> ingredients, List<IRecipeIngredient> result, int recipeWidth, int recipeHeight) {
        super(ingredients, result, recipeWidth, recipeHeight);
    }

    /**
     * Checks if the given crafting input matches this recipe. Checks all possible offsets in the input.
     * @param input the crafting input to check
     * @param level the level the crafting is taking place in
     * @return true if the input matches this recipe, false otherwise
     */
    @Override
    public boolean matches(CraftingInput input, @NotNull Level level) {
        int inputWidth = input.width();
        int inputHeight = input.height();

        if (inputWidth < recipeWidth || inputHeight < recipeHeight) return false;

        for (int xOffset = 0; xOffset <= inputWidth - recipeWidth; ++xOffset) {
            for (int yOffset = 0; yOffset <= inputHeight - recipeHeight; ++yOffset) {
                if (matchesAt(input, xOffset, yOffset)) return true;
            }
        }
        return false;
    }

    private boolean matchesAt(CraftingInput input, int xOffset, int yOffset) {
        int inputWidth = input.width();
        int inputHeight = input.height();

        if (xOffset + recipeWidth > inputWidth || yOffset + recipeHeight > inputHeight) return false;

        for (int y = yOffset; y < recipeHeight; y++) {
            for (int x = xOffset; x < recipeWidth; x++) {
                IRecipeIngredient expected = ingredients.get(y * recipeWidth + x);
                ItemStack inSlot = input.getItem(x, y);
                if (!expected.test(inSlot)) return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return CustomRecipes.RECIPE_SHAPED_SERIALIZER.get();
    }

    @Override
    public @NotNull CraftingBookCategory category() {
        return null; // TODO implement categories
    }

    @Override
    public @NotNull RecipeType<CraftingRecipe> getType() {
        return CustomRecipes.RECIPE_SHAPED.get();
    }
}
