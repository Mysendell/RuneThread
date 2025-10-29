package io.github.runethread.gui.slots;

import io.github.runethread.customblocks.craftingtable.ICraftingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;


/**
 * A CraftingSlot that only allows item extraction, not placement.
 * @see ISlotType
 * @see CraftingSlot
 */
public class OutputSlot extends CraftingSlot {

    /**
     * Creates a new OutputSlot.
     * @param itemHandler the item handler assigned to this slot
     * @param slotIndex the slot index inside that item handler
     * @param x the x position of the slot on the GUI
     * @param y the y position of the slot on the GUI
     * @param blockEntity the crafting entity this slot belongs to
     */
    public OutputSlot(IItemHandler itemHandler, int slotIndex, int x, int y, ICraftingEntity blockEntity) {
        super(itemHandler, slotIndex, x, y, blockEntity);
    }


    /**
     * Always returns false, preventing any item placement in this slot. <br>
     * Note that the ItemHandler <b>can</b> still be modified and make items appear in this slot, this only prevents player interaction.
     * @param stack the ItemStack to check
     * @return false, as no items can be placed in this slot
     */
    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }
}