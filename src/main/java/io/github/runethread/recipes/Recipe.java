package io.github.runethread.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public abstract class Recipe<T extends RecipeInput> implements net.minecraft.world.item.crafting.Recipe<T> {
    protected final ResourceLocation id;
    protected final List<Ingredient> ingredients;
    protected final ItemStack result;
    protected final int width, height;
    protected final int gridWidth = 3;
    protected final int gridHeight = 3;
    protected PlacementInfo placementInfo;

    public Recipe(ResourceLocation id, List<Ingredient> ingredients, ItemStack result, int width, int height) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
        this.width = width;
        this.height = height;
    }
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ItemStack getResult() {
        return result.copy();
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public ItemStack assemble(T input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.create(this.ingredients);
        }
        return this.placementInfo;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    public ResourceLocation getId() {
        return id;
    }
}
