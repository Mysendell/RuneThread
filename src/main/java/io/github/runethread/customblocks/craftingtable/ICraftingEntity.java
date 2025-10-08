package io.github.runethread.customblocks.craftingtable;

import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;

public interface ICraftingEntity {
    ItemStackHandler getOutput();
    ItemStackHandler getInput();
    void scheduleCraftingUpdate();
    Level getLevel();
}
