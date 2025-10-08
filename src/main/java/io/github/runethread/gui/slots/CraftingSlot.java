package io.github.runethread.gui.slots;

import io.github.runethread.customblocks.craftingtable.ICraftingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

/***
 * A slot specific to crafting entities, causes the entity to update its recipe when changed
 * Can receive a ISlotType to restrict what items can be placed in it
 * @see ICraftingEntity
 * @see ISlotType
 */
public class CraftingSlot extends SlotItemHandler {
    ICraftingEntity blockEntity;
    ISlotType slotType;

    /***
     * Creates a crafting slot with a specific slot type
     * @param itemHandler the item handler assigned to this slot
     * @param slotIndex the slot index inside that item handler
     * @param x X position on the GUI
     * @param y Y position on the GUI
     * @param blockEntity the crafting entity this slot belongs to
     * @param slotType the slot type defining placement rules
     * @see ISlotType
     */
    public CraftingSlot(IItemHandler itemHandler, int slotIndex, int x, int y, ICraftingEntity blockEntity, ISlotType slotType) {
        super(itemHandler, slotIndex, x, y);
        this.blockEntity = blockEntity;
        this.slotType = slotType;
    }

    /***
     * Creates a crafting slot with no placement restrictions
     * @param itemHandler the item handler assigned to this slot
     * @param slot the slot index inside that item handler
     * @param x X position on the GUI
     * @param y Y position on the GUI
     * @param blockEntity the crafting entity this slot belongs to
     */
    public CraftingSlot(IItemHandler itemHandler, int slot, int x, int y, ICraftingEntity blockEntity) {
        super(itemHandler, slot, x, y);
        this.blockEntity = blockEntity;
        this.slotType = new BlockEntitySlot(itemHandler, slot, x, y, (BlockEntity) blockEntity);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return super.mayPlace(stack) && slotType.mayPlace(stack);
    }

    /***
     * Sets the content of this slot. Schedules a crafting update in the associated block entity if not on client side.
     * @param stack the ItemStack to set in this slot
     */
    @Override
    public void set(@NotNull ItemStack stack) {
        super.set(stack);
        if (!blockEntity.getLevel().isClientSide()) {
            blockEntity.scheduleCraftingUpdate();
        }
    }

    /***
     * Called when the player takes an item from the slot. Schedules a crafting update in the associated block entity if not on client side.
     * @param player the player taking the item
     * @param stack the ItemStack being taken
     */
    @Override
    public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        super.onTake(player, stack);
        if (!blockEntity.getLevel().isClientSide()) {
            blockEntity.scheduleCraftingUpdate();
        }
    }
}