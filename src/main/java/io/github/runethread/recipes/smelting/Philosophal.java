package io.github.runethread.recipes.smelting;

import io.github.runethread.recipes.RecipeResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.List;

import static io.github.runethread.recipes.CustomRecipes.*;

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
}
