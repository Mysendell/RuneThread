package io.github.runethread.recipes;

import io.github.runethread.ItemStackHandlerContainer;
import io.github.runethread.customblocks.craftingtable.CraftingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CraftingOutputSlot<J extends CraftingEntity> extends Slot {
    private final J blockEntity;
    private final Level level;
    private final Player player;

    public CraftingOutputSlot(J blockEntity, Player player, Level level, int slotIndex, int x, int y) {
        super(new ItemStackHandlerContainer(blockEntity.getOutput()), slotIndex, x, y);
        this.blockEntity = blockEntity;
        this.level = level;
        this.player = player;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false; // Prevent placing items in output
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        blockEntity.doCraft(level);
        super.onTake(player, stack);
    }

    @Override
    public void onQuickCraft(ItemStack stack, int amount) {
        blockEntity.doCraftShift(level, player);
        super.onQuickCraft(stack, amount);
    }
}