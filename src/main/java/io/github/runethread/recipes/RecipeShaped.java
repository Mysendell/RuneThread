package io.github.runethread.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class RecipeShaped<T extends RecipeInput> extends Recipe<T>{


    public RecipeShaped(ResourceLocation id, List<Ingredient> ingredients, ItemStack result, int width, int height) {
        super(id, ingredients, result, width, height);
    }

    public boolean matches(T input, Level level) {
        for (int xOffset = 0; xOffset <= gridWidth - width; ++xOffset) {
            for (int yOffset = 0; yOffset <= gridHeight - height; ++yOffset) {
                if (matchesAt(input, xOffset, yOffset, gridWidth, gridHeight)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchesAt(T input, int xOffset, int yOffset, int gridWidth, int gridHeight) {
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
}
