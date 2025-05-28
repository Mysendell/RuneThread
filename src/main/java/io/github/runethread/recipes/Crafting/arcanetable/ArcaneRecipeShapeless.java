package io.github.runethread.recipes.Crafting.arcanetable;

import io.github.runethread.recipes.Crafting.RecipeShapeless;
import io.github.runethread.recipes.CustomRecipes;
import io.github.runethread.recipes.RecipeResult;
import net.minecraft.world.item.crafting.*;

import java.util.List;

public class ArcaneRecipeShapeless extends RecipeShapeless {
    public ArcaneRecipeShapeless(List<Ingredient> ingredients, List<RecipeResult> results, int width, int height) {
        super(ingredients, results, width, height);
    }

    @Override
    public RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return CustomRecipes.ARCANE_SHAPELESS_SERIALIZER.get();
    }

    @Override
    public CraftingBookCategory category() {
        return null;
    }

    @Override
    public RecipeType<CraftingRecipe> getType() {
        return CustomRecipes.ARCANE_SHAPELESS.get();
    }
}
