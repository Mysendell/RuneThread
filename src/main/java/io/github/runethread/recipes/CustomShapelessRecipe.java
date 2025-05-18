package io.github.runethread.recipes;

// CustomShapelessRecipe.java

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingContainer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public class CustomShapelessRecipe extends AbstractCustomRecipe {
    private final List<ItemStack> ingredients;

    public CustomShapelessRecipe(ResourceLocation id, ItemStack output, List<ItemStack> ingredients) {
        super(id, output);
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        // Simplified match logic
        return true;
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CustomRecipeSerializers.SHAPELESS.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CustomRecipeTypes.SHAPELESS.get();
    }
}

