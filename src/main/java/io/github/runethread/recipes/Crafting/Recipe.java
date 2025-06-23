package io.github.runethread.recipes.Crafting;

import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.PowerData;
import io.github.runethread.recipes.CustomRecipes;
import io.github.runethread.recipes.RecipeResult;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.ArrayList;
import java.util.List;

import static io.github.runethread.customitems.CustomItems.POWER_RUNE;

public abstract class Recipe implements CraftingRecipe {
    protected final List<Ingredient> ingredients;
    protected final List<RecipeResult> results;
    protected final int width, height;
    protected int gridWidth = 3;
    protected int gridHeight = 3;
    protected PlacementInfo placementInfo;

    public Recipe(List<Ingredient> ingredients, List<RecipeResult> results, int width, int height) {
        this.ingredients = ingredients;
        this.results = results;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getGridWidth() {
        return gridWidth;
    }
    public int getGridHeight() {
        return gridHeight;
    }

    public List<ItemStack> getResultItemStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        for (RecipeResult result : results) {
            stacks.add(result.toStack());
        }
        return stacks;
    }

    public List<RecipeResult> getResultStack() {
        return results;
    }

    public ItemStack getResult(HolderLookup.Provider registries) {
        if (results.isEmpty()) return ItemStack.EMPTY;
        return results.getFirst().toStack();
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<ItemStack> assembleStack(CraftingInput input, HolderLookup.Provider registries) {
        return getResultItemStacks();
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack itemStack = getResult(registries);
        Item item = itemStack.getItem();
        if(item == POWER_RUNE.get())
            itemStack.set(DataComponentRegistry.POWER_DATA.get(), new PowerData(results.getFirst().variation()));
        return itemStack;
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
        return CustomRecipes.RUNETHREAD_CATEGORY.get();
    }
}
