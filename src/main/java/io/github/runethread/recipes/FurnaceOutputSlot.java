package io.github.runethread.recipes;

import io.github.runethread.ItemStackHandlerContainer;
import io.github.runethread.customblocks.craftingtable.FurnaceEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FurnaceOutputSlot<J extends FurnaceEntity> extends Slot {
    private final J blockEntity;
    private final Level level;
    private final Player player;

    public FurnaceOutputSlot(J blockEntity, Player player, Level level, int slotIndex, int x, int y) {
        super(new ItemStackHandlerContainer(blockEntity.getOutput()), slotIndex, x, y);
        this.blockEntity = blockEntity;
        this.level = level;
        this.player = player;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false; // Prevent placing items in output
    }
}