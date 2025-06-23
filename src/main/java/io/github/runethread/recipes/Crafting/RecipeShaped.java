package io.github.runethread.recipes.Crafting;

import io.github.runethread.recipes.RecipeResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class RecipeShaped extends Recipe{


    public RecipeShaped(List<Ingredient> ingredients, List<RecipeResult> result, int width, int height) {
        super(ingredients, result, width, height);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        // Vanilla-style: grid might be smaller than recipe
        int inputWidth = input.width();
        int inputHeight = input.height();

        if (inputWidth < width || inputHeight < height) return false;

        // Try all possible subgrids where the recipe could fit
        for (int xOffset = 0; xOffset <= inputWidth - width; ++xOffset) {
            for (int yOffset = 0; yOffset <= inputHeight - height; ++yOffset) {
                if (matchesAt(input, xOffset, yOffset)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchesAt(CraftingInput input, int xOffset, int yOffset) {
        // Only access inside input's bounds!
        int inputWidth = input.width();
        int inputHeight = input.height();

        if (xOffset + width > inputWidth || yOffset + height > inputHeight) return false;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int subX = x + xOffset;
                int subY = y + yOffset;
                Ingredient expected = ingredients.get(y * width + x);
                ItemStack inSlot = input.getItem(subX, subY); // Use (x, y) form, not flat index
                if (!expected.test(inSlot)) return false;
            }
        }
        return true;
    }
}
