package io.github.runethread.recipes.Crafting;

import io.github.runethread.recipes.CustomRecipes;
import io.github.runethread.recipes.IRecipeIngredient;
import io.github.runethread.recipes.RecipeItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A base class for custom crafting recipes.
 * Implements CraftingRecipe and provides common functionality for handling ingredients and results.
 * Does not implement matches(), for that check specific recipe types.
 */
public abstract class ModRecipe implements CraftingRecipe {
    protected final List<IRecipeIngredient> ingredients;
    protected final List<IRecipeIngredient> results;
    protected final int recipeWidth, recipeHeight;
    protected PlacementInfo placementInfo;

    public ModRecipe(List<IRecipeIngredient> ingredients, List<IRecipeIngredient> results, int recipeWidth, int recipeHeight) {
        this.ingredients = ingredients;
        this.results = results;
        this.recipeWidth = recipeWidth;
        this.recipeHeight = recipeHeight;
    }

    /**
     * Gets a list of all result ItemStacks for this recipe.
     * @return A list of ItemStacks representing the results of this recipe.
     */
    public List<ItemStack> getResultItemStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        for (IRecipeIngredient result : results) {
            stacks.add(((RecipeItem) result).toStack());
        }
        return stacks;
    }

    public ItemStack getResultItemStack() {
        if (results.isEmpty()) return ItemStack.EMPTY;
        return results.getFirst().toStack();
    }

    public List<ItemStack> assembleStacks() {
        return getResultItemStacks();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput input, HolderLookup.@NotNull Provider registries) {
        return getResultItemStack();
    }

    @Override
    public PlacementInfo placementInfo() { // TODO implement placementinfo for tags and other ingredient types
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return CustomRecipes.RUNETHREAD_CATEGORY.get();
    }

    /**
     * Gets the list of RecipeResult objects for this recipe. Only used by serializers.
     * @return A list of RecipeResult objects representing the results of this recipe.
     */
    public List<IRecipeIngredient> getResults() {
        return results;
    }

    private static int[] getCountMap(int width, int height, List<IRecipeIngredient> ingredients) {
        int[] countMap = new int[width * height];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int idx = row * width + col;
                IRecipeIngredient ingredient = ingredients.get(idx);
                countMap[idx] = ingredient.count();
            }
        }
        return countMap;
    }

    public int[] getResultCountMap() {
        return getCountMap(recipeWidth, recipeHeight, results);
    }

    public int[] getIngredientCountMap() {
        return getCountMap(recipeWidth, recipeHeight, ingredients);
    }

    public int getRecipeWidth() {
        return recipeWidth;
    }
    public int getRecipeHeight() {
        return recipeHeight;
    }

    public List<IRecipeIngredient> getIngredients() {
        return ingredients;
    }
}
