package io.github.runethread.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class ArcaneRecipeShaped implements Recipe<ArcaneRecipeShapedInput> {
    private final ResourceLocation id;
    private final List<Ingredient> ingredients;
    private final ItemStack result;
    private final int width, height;
    private final int gridWidth = 3;
    private final int gridHeight = 3;
    private PlacementInfo placementInfo;

    public ArcaneRecipeShaped(ResourceLocation id, List<Ingredient> ingredients, ItemStack result, int width, int height) {
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

    // For codec: this returns a function for the result
    public Function<RegistryAccess, ItemStack> getResultFunction() {
        return (reg) -> result.copy();
    }

    public ItemStack getResult() {
        return result.copy();
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public boolean matches(ArcaneRecipeShapedInput input, Level level) {
        for (int xOffset = 0; xOffset <= gridWidth - width; ++xOffset) {
            for (int yOffset = 0; yOffset <= gridHeight - height; ++yOffset) {
                if (matchesAt(input, xOffset, yOffset, gridWidth, gridHeight)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchesAt(ArcaneRecipeShapedInput input, int xOffset, int yOffset, int gridWidth, int gridHeight) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int subX = x + xOffset;
                int subY = y + yOffset;
                if (subX >= gridWidth || subY >= gridHeight) return false;
                int idx = subX + subY * gridWidth;
                Ingredient expected = ingredients.get(x + y * width);
                ItemStack inSlot = input.getItem(idx);
                if (!expected.test(inSlot)) return false;
            }
        }
        return true;
    }
    @Override
    public ItemStack assemble(ArcaneRecipeShapedInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<ArcaneRecipeShapedInput>> getSerializer() {
        return CustomRecipes.ARCANE_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<ArcaneRecipeShapedInput>> getType() {
        return CustomRecipes.ARCANE_SHAPED.get();
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