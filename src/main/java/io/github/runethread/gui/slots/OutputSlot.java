package io.github.runethread.gui.slots;

import io.github.runethread.customblocks.craftingtable.ICraftingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;


/**
 * A CraftingSlot that optionally allows placement.
 * @see ISlotType
 * @see CraftingSlot
 */
public class OutputSlot extends CraftingSlot {

    /**
     * Creates a new OutputSlot.
     *
     * @param itemHandler the item handler assigned to this slot
     * @param slotIndex   the slot index inside that item handler
     * @param x           the x position of the slot on the GUI
     * @param y           the y position of the slot on the GUI
     * @param blockEntity the crafting entity this slot belongs to
     */
    public OutputSlot(IItemHandler itemHandler, int slotIndex, int x, int y, ICraftingEntity blockEntity, ISlotType slotType, Inventory playerInv) {
        super(itemHandler, slotIndex, x, y, blockEntity, slotType, playerInv);
    } // TODO these javadocs are inaccurate

    public OutputSlot(IItemHandler itemHandler, int slotIndex, int x, int y, ICraftingEntity blockEntity, boolean mayPlace, Inventory playerInv) {
        super(itemHandler, slotIndex, x, y, blockEntity, mayPlace, playerInv);
    }

    @Override
    public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        blockEntity.doCraft(stack, player);
        super.onTake(player, stack);
    }
}