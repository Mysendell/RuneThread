package io.github.runethread.recipes;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public class RecipeInput implements net.minecraft.world.item.crafting.RecipeInput {
    private final List<ItemStack> items;
    public RecipeInput(List<ItemStack> items) { this.items = items; }
    @Override public ItemStack getItem(int i) { return items.get(i); }
    @Override public int size() { return items.size(); }
}
