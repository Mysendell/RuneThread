package io.github.runethread.recipes.Crafting;

import io.github.runethread.recipes.CustomRecipes;
import io.github.runethread.recipes.RecipeResult;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/***
 * A base class for custom crafting recipes.
 * Implements CraftingRecipe and provides common functionality for handling ingredients and results.
 * Does not implement matches(), for that check specific recipe types.
 */
public abstract class Recipe implements CraftingRecipe {
    protected final List<Ingredient> ingredients;
    protected final List<RecipeResult> results;
    protected final int recipeWidth, recipeHeight;
    protected PlacementInfo placementInfo;

    public Recipe(List<Ingredient> ingredients, List<RecipeResult> results, int recipeWidth, int recipeHeight) {
        this.ingredients = ingredients;
        this.results = results;
        this.recipeWidth = recipeWidth;
        this.recipeHeight = recipeHeight;
    }

    /***
     * Gets a list of all result ItemStacks for this recipe.
     * @return A list of ItemStacks representing the results of this recipe.
     */
    public List<ItemStack> getResultItemStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        for (RecipeResult result : results) {
            stacks.add(result.toStack());
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
    public @NotNull PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.create(this.ingredients);
        }
        return this.placementInfo;
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return CustomRecipes.RUNETHREAD_CATEGORY.get();
    }

    /***
     * Gets the list of RecipeResult objects for this recipe. Only used by serializers.
     * @return A list of RecipeResult objects representing the results of this recipe.
     */
    public List<RecipeResult> getResults() {
        return results;
    }

    public int getRecipeWidth() {
        return recipeWidth;
    }
    public int getRecipeHeight() {
        return recipeHeight;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
