package io.github.runethread.customitems.runes.functionrunes;

import io.github.runethread.util.ILocation;
import io.github.runethread.util.runeutils.ModifierMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

@FunctionalInterface
public interface RuneFunction {
    RitualState perform(ServerLevel level, ServerPlayer player, ItemStack mainStack, double finalScale, ILocation destination, @Nullable ILocation reference, BlockPos origin, ModifierMap additionalData);
}
