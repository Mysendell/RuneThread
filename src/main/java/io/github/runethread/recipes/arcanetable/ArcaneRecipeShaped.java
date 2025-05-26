package io.github.runethread.recipes.arcanetable;

import io.github.runethread.recipes.CustomRecipes;
import io.github.runethread.recipes.RecipeShaped;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public class ArcaneRecipeShaped extends RecipeShaped<io.github.runethread.recipes.RecipeInput> {
    public ArcaneRecipeShaped(ResourceLocation id, List<Ingredient> ingredients, ItemStack result, int width, int height) {
        super(id, ingredients, result, width, height);
    }
    @Override
    public RecipeSerializer<? extends Recipe<io.github.runethread.recipes.RecipeInput>> getSerializer() {
        return CustomRecipes.ARCANE_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<io.github.runethread.recipes.RecipeInput>> getType() {
        return CustomRecipes.ARCANE_SHAPED.get();
    }

}