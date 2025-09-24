package io.github.runethread.customitems.runes;

import io.github.runethread.customblocks.altar.RunicAltarEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Map;

@FunctionalInterface
public interface RuneFunction {
    RunicAltarEntity.RitualState perform(ServerLevel level, ServerPlayer player, ItemStack item, double finalScale, RunicAltarEntity.DestinationRuneData destination, @Nullable RunicAltarEntity.DestinationRuneData reference, Map<String, Object> additionalData);
} // TODO break down this function into smaller parts for better readability
