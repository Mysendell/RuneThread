package io.github.runethread.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ArcaneRecipeShapedInput implements RecipeInput {
    private final List<ItemStack> items;
    public ArcaneRecipeShapedInput(List<ItemStack> items) { this.items = items; }
    @Override public ItemStack getItem(int i) { return items.get(i); }
    @Override public int size() { return items.size(); }
}