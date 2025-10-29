package io.github.runethread.recipes.smelting;

import io.github.runethread.recipes.CustomRecipes;
import io.github.runethread.recipes.IRecipeIngredient;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.github.runethread.recipes.CustomRecipes.PHILOSOPHAL;
import static io.github.runethread.recipes.CustomRecipes.PHILOSOPHAL_SERIALIZER;

public class PhilosophalRecipe extends SmeltingRecipe {
    public PhilosophalRecipe(List<IRecipeIngredient> ingredients, List<IRecipeIngredient>  result, int width, int height, int burnTime, int fuelBurnMultiplier) {
        super(ingredients, result, width, height, burnTime, fuelBurnMultiplier);
    }

    @Override
    public @NotNull RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return PHILOSOPHAL_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<CraftingRecipe> getType() {
        return PHILOSOPHAL.get();
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return CustomRecipes.RUNETHREAD_CATEGORY.get();
    }
}
