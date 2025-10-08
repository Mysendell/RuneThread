package io.github.runethread.gui.slots;

import net.minecraft.world.item.ItemStack;

/***
 * A slot type interface to define custom slot behaviors.
 */
public interface ISlotType {
    boolean mayPlace(ItemStack stack);
}
