package io.github.runethread.customblocks.craftingtable;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public interface ICraftingEntity {
    ItemStackHandler getOutput();
    ItemStackHandler getInput();
    void scheduleCraftingUpdate(@NotNull ItemStack stack, boolean taken, Inventory playerInv);
    void doCraft(Level level, @NotNull ItemStack stack, Player player);
    Level getLevel();
}
