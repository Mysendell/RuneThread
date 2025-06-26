package io.github.runethread.customitems.runes;

import io.github.runethread.customblocks.craftingtable.altar.RunicAltarEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;

@FunctionalInterface
public interface RuneFunction {
    void perform(ServerLevel level, ServerPlayer player, MainRuneItem item, int finalScale, RunicAltarEntity.DestinationRuneData destination, @Nullable RunicAltarEntity.DestinationRuneData reference, Object[] additionalData);
} // TODO break down this function into smaller parts for better readability
