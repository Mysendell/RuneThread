package io.github.runethread.recipes.smelting;

import io.github.runethread.recipes.Crafting.RecipeShaped;
import io.github.runethread.recipes.RecipeResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.List;

import static io.github.runethread.recipes.CustomRecipes.SMELTING;
import static io.github.runethread.recipes.CustomRecipes.SMELTING_SERIALIZER;

public class Smelting extends RecipeShaped {
    protected PlacementInfo placementInfo;
    protected final int burnTime;
    protected final int fuelBurnMultiplier;

    public Smelting(List<Ingredient> ingredients, List<RecipeResult> result, int width, int height, int burnTime, int fuelBurnMultiplier) {
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
    public RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return SMELTING_SERIALIZER.get();
    }

    @Override
    public CraftingBookCategory category() {
        return null;
    }

    public RecipeType<CraftingRecipe> getType() {
        return SMELTING.get();
    }

}
