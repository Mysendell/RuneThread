package io.github.runethread.customitems.runes.functionrunes;

import io.github.runethread.customblocks.altar.RunicAltarEntity;
import io.github.runethread.util.ILocation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Map;

@FunctionalInterface
public interface RuneFunction {
    RunicAltarEntity.RitualState perform(ServerLevel level, ServerPlayer player, ItemStack mainStack, double finalScale, ILocation destination, @Nullable ILocation reference, BlockPos origin, Map<String, Object> additionalData);
}
