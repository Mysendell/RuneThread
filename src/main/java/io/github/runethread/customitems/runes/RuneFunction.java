package io.github.runethread.customitems.runes;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

@FunctionalInterface
public interface RuneFunction {
    void perform(ServerLevel level, ServerPlayer player, MainRuneItem item, int finalScale, int posX, int posY, int posZ, Object[] additionalData);
}
