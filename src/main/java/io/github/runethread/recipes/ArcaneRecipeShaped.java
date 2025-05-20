package io.github.runethread.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ArcaneRecipeShaped implements Recipe<CraftingInput> {
    private final ResourceLocation id;
    private final List<Ingredient> ingredients;
    private final ItemStack result;
    private final int craftTime;


    public ArcaneRecipeShaped(ResourceLocation id, List<Ingredient> ingredients, ItemStack result, int craftTime) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
        this.craftTime = craftTime;
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

    public boolean matches(Container input, Level level) {
        return false;
    }

    public ItemStack assemble(Container input, HolderLookup.Provider registries) {
        return null;
    }

    public boolean matches(SimpleContainer input, Level level) {
        return false;
    }

    public ItemStack assemble(SimpleContainer input, HolderLookup.Provider registries) {
        return null;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return null;
    }

    @Override
    public RecipeSerializer<? extends Recipe<CraftingInput>> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<? extends Recipe<CraftingInput>> getType() {
        return null;
    }

    @Override
    public PlacementInfo placementInfo() {
        return null;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }
}
