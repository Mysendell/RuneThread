package io.github.runethread.recipes.Crafting.arcanetable;

import io.github.runethread.recipes.CustomRecipes;
import io.github.runethread.recipes.Crafting.RecipeShaped;
import io.github.runethread.recipes.RecipeResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.RecipeBook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.List;

public class ArcaneRecipeShaped extends RecipeShaped{
    public ArcaneRecipeShaped(List<Ingredient> ingredients, List<RecipeResult> result, int width, int height) {
        super(ingredients, result, width, height);
    }
    @Override
    public RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return CustomRecipes.ARCANE_RECIPE_SERIALIZER.get();
    }

    @Override
    public CraftingBookCategory category() {
        return null;
    }

    public RecipeType<CraftingRecipe> getType() {
        return CustomRecipes.ARCANE_SHAPED.get();
    }

}