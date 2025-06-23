package io.github.runethread.recipes.smelting;

import io.github.runethread.recipes.CustomRecipes;
import io.github.runethread.recipes.RecipeResult;
import net.minecraft.world.item.crafting.*;

import java.util.List;

import static io.github.runethread.recipes.CustomRecipes.PHILOSOPHAL;
import static io.github.runethread.recipes.CustomRecipes.PHILOSOPHAL_SERIALIZER;

public class Philosophal extends Smelting{


    public Philosophal(List<Ingredient> ingredients, List<RecipeResult>  result, int width, int height, int burnTime, int fuelBurnMultiplier) {
        super(ingredients, result, width, height, burnTime, fuelBurnMultiplier);
    }

    @Override
    public RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return PHILOSOPHAL_SERIALIZER.get();
    }

    @Override
    public RecipeType<CraftingRecipe> getType() {
        return PHILOSOPHAL.get();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return CustomRecipes.RUNETHREAD_CATEGORY.get();
    }
}
