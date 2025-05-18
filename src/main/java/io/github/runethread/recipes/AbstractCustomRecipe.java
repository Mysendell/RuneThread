package io.github.runethread.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public abstract class AbstractCustomRecipe implements Recipe<CraftingInput> {
    protected final ResourceLocation id;
    protected final ItemStack output;

    public AbstractCustomRecipe(ResourceLocation id, ItemStack output) {
        this.id = id;
        this.output = output;
    }

    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
    }

    public ResourceLocation getId() {
        return id;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        return output.copy();
    }

    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.create();
    }
}

