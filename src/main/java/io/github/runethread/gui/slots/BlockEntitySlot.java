package io.github.runethread.gui.slots;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

/**
 * A Slot that is linked to a BlockEntity marks it as changed when its contents change.
 * @see ISlotType
 */
public class BlockEntitySlot extends SlotItemHandler implements ISlotType{
    private final BlockEntity blockEntity;

    /**
     * Creates a new BlockEntitySlot.
     * @param itemHandler the item handler assigned to this slot
     * @param slotIndex the slot index inside that item handler
     * @param xPosition the x position of the slot on the GUI
     * @param yPosition the y position of the slot on the GUI
     * @param blockEntity the block entity this slot belongs to
     */
    public BlockEntitySlot(IItemHandler itemHandler, int slotIndex, int xPosition, int yPosition, BlockEntity blockEntity) {
        super(itemHandler, slotIndex, xPosition, yPosition);
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return true;
    }

    /**
     * Sets the content of this slot. Marks the block entity as changed if not on client side.
     * @param stack the ItemStack to set in this slot
     */
    @Override
    public void set(@NotNull ItemStack stack) {
        super.set(stack);
        assert blockEntity.getLevel() != null;
        if (!blockEntity.getLevel().isClientSide()) {
            blockEntity.setChanged();
        }
    }

    /**
     * Called when the player takes an item from the slot. Marks the block entity as changed if not on client side.
     * @param player the player taking the item
     * @param stack the ItemStack being taken
     */
    @Override
    public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        super.onTake(player, stack);
        assert blockEntity.getLevel() != null;
        if (!blockEntity.getLevel().isClientSide()) {
            blockEntity.setChanged();
        }
    }
}
