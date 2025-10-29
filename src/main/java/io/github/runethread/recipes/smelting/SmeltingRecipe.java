package io.github.runethread.recipes.smelting;

import io.github.runethread.recipes.Crafting.RecipeShaped;
import io.github.runethread.recipes.CustomRecipes;
import io.github.runethread.recipes.IRecipeIngredient;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.github.runethread.recipes.CustomRecipes.SMELTING;
import static io.github.runethread.recipes.CustomRecipes.SMELTING_SERIALIZER;

/**
 * A smelting recipe. Similar to a shaped crafting recipe, but includes burn time and fuel burn multiplier.
 * @see RecipeShaped
 */
public class SmeltingRecipe extends RecipeShaped {
    protected final int burnTime;
    protected final int fuelBurnMultiplier;

    public SmeltingRecipe(List<IRecipeIngredient> ingredients, List<IRecipeIngredient> result, int width, int height, int burnTime, int fuelBurnMultiplier) {
        super(ingredients, result, width, height);
        this.burnTime = burnTime;
        this.fuelBurnMultiplier = fuelBurnMultiplier;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public int getFuelBurnMultiplier() {
        return fuelBurnMultiplier;
    }

    @Override
    public @NotNull RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return SMELTING_SERIALIZER.get();
    }

    @Override
    public @NotNull CraftingBookCategory category() {
        return null;
    }

    public @NotNull RecipeType<CraftingRecipe> getType() {
        return SMELTING.get();
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return CustomRecipes.RUNETHREAD_CATEGORY.get();
    }
}
