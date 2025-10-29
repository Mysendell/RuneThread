package io.github.runethread.recipes;

import net.minecraft.world.item.ItemStack;

public interface IRecipeIngredient {
    boolean test(ItemStack stack);
    int count();
    ItemStack toStack();
}
