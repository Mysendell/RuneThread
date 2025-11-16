package io.github.runethread.customitems.runes.functionrunes;

import io.github.runethread.customblocks.altar.RunicAltarEntity;
import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.util.ExplosionUtil;
import io.github.runethread.util.ILocation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DestructionRuneItem extends MainRuneItem{
    public DestructionRuneItem(Properties properties, int cost, double scale, double scalingCost, double breakChance) {
        super(properties, cost, scale, scalingCost, breakChance);
    }

    @Override
    public RunicAltarEntity.RitualState perform(ServerLevel level, ServerPlayer player, ItemStack mainStack, double finalScale, ILocation destination, @Nullable ILocation reference, BlockPos origin, Map<String, Object> additionalData) {
        MainRuneItem item = (MainRuneItem) mainStack.getItem();
        ExplosionUtil.accurateExplosion(level, destination.getLocation(level), finalScale * item.getScale(),
                false, true, true, item.getScale() * finalScale * 1.5f);
        return RunicAltarEntity.RitualState.SUCCESS;
    }
}
