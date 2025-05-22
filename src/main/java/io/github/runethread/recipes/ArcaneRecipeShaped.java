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

public class ArcaneRecipeShaped implements Recipe<CraftingInput> {
    private final @Nullable ResourceLocation id;
    private final List<Ingredient> ingredients;
    private final ItemStack result;
    private final int craftTime;
    private final int width, height;

    public ArcaneRecipeShaped(@Nullable ResourceLocation id, List<Ingredient> ingredients, ItemStack result, int craftTime, int width, int height) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
        this.craftTime = craftTime;
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

    public int getCraftTime() {
        return craftTime;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int gridWidth = (int) Math.sqrt(input.size()); // Only works for square grids, adapt as needed!
        int gridHeight = gridWidth; // If not square, get height differently.

        for (int xOffset = 0; xOffset <= gridWidth - width; ++xOffset) {
            for (int yOffset = 0; yOffset <= gridHeight - height; ++yOffset) {
                if (matchesAt(input, xOffset, yOffset, gridWidth, gridHeight)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchesAt(CraftingInput input, int xOffset, int yOffset, int gridWidth, int gridHeight) {
        for (int x = 0; x < gridWidth; ++x) {
            for (int y = 0; y < gridHeight; ++y) {
                int subX = x - xOffset;
                int subY = y - yOffset;
                Ingredient expected = Ingredient.of(); // empty by default

                if (subX >= 0 && subY >= 0 && subX < width && subY < height) {
                    int idx = subX + subY * width;
                    expected = ingredients.get(idx);
                }

                ItemStack inSlot = input.getItem(x + y * gridWidth);
                if (!expected.test(inSlot)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<CraftingInput>> getSerializer() {
        return CustomRecipes.ARCANE_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<CraftingInput>> getType() {
        return CustomRecipes.ARCANE_SHAPED.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return null;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    public ResourceLocation getId() {
        return id;
    }
}