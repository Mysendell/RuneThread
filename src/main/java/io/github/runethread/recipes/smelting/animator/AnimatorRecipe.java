package io.github.runethread.recipes.smelting.animator;

import io.github.runethread.recipes.RecipeResult;
import io.github.runethread.recipes.smelting.Philosophal;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

import static io.github.runethread.recipes.CustomRecipes.*;

public class AnimatorRecipe extends Philosophal {
    public AnimatorRecipe(List<Ingredient> ingredients, List<RecipeResult> result, int width, int height, int burnTime, int fuelBurnMultiplier) {
        super(ingredients, result, width, height, burnTime, fuelBurnMultiplier);
        this.gridHeight = 1;
        this.gridWidth = 1;
    }

    @Override
    public RecipeType<CraftingRecipe> getType() {
        return ANIMATOR_RECIPE.get();
    }

    @Override
    public RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return ANIMATOR_SERIALIZER.get();
    }
}
