package io.github.runethread.gui.slots;

import io.github.runethread.customblocks.craftingtable.CraftingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;


/***
 * A CraftingSlot that only allows item extraction, not placement. Supports shift-click crafting.
 * @see CraftingEntity
 * @see CraftingSlot
 */
public class CraftingOutputSlot extends CraftingSlot {
    private final CraftingEntity blockEntity;
    private final Level level;
    private final Player player;

    /***
     * Creates a new CraftingOutputSlot.
     * @param itemHandler the item handler assigned to this slot
     * @param player the player interacting with the GUI
     * @param level the level the block entity is in
     * @param slotIndex the slot index inside that item handler
     * @param x the x position of the slot on the GUI
     * @param y the y position of the slot on the GUI
     * @param blockEntity the crafting entity this slot belongs to
     */
    public CraftingOutputSlot(IItemHandler itemHandler, Player player, Level level, int slotIndex, int x, int y, CraftingEntity blockEntity) {
        super(itemHandler, slotIndex, x, y, blockEntity);
        this.blockEntity = blockEntity;
        this.level = level;
        this.player = player;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    /***
     * Called when the player takes an item from the slot. Triggers crafting in the associated block entity.
     * @param player the player taking the item
     * @param stack the ItemStack being taken
     */
    @Override
    public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        blockEntity.doCraft(level);
        super.onTake(player, stack);
    }

    /***
     * Called when the player shift-clicks an item from the slot. Triggers crafting in the associated block entity.
     * @param stack the ItemStack being taken
     * @param amount the amount of items being taken
     */
    @Override
    public void onQuickCraft(@NotNull ItemStack stack, int amount) {
        blockEntity.doCraftShift(level, player);
        super.onQuickCraft(stack, amount);
    }
}