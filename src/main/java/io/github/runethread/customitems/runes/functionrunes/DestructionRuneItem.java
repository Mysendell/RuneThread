package io.github.runethread.customitems.runes.functionrunes;

import io.github.runethread.customblocks.altar.RunicAltarEntity;
import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.util.ILocation;
import io.github.runethread.util.runeutils.ExplosionUtil;
import io.github.runethread.util.runeutils.ModifierMap;
import io.github.runethread.util.runeutils.ModifierRune;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static io.github.runethread.util.runeutils.ModifierRune.*;

public class DestructionRuneItem extends MainRuneItem{
    public DestructionRuneItem(Properties properties, int cost, double scale, double scalingCost, double breakChance) {
        super(properties, cost, scale, scalingCost, breakChance);
    }

    @Override
    public RunicAltarEntity.RitualState perform(ServerLevel level, ServerPlayer player, ItemStack mainStack, double finalScale, ILocation destination, @Nullable ILocation reference, BlockPos origin, ModifierMap additionalData) {
        boolean damageEntities = additionalData.hasModifier(LIVING_RUNE);
        boolean dropBlocks = additionalData.hasModifier(NULL_RUNE);
        List<ResourceLocation> filter = additionalData.getAllIdentifiersOfType(BlockItem.class);
        boolean reversedFilter = additionalData.hasModifier(REVERSE_RUNE);
        ModifierRune shape = additionalData.getShape();
        ExplosionUtil.accurateExplosion(level, destination.getLocation(level), finalScale * getScale(),
                dropBlocks, filter, reversedFilter, shape, damageEntities, getScale() * finalScale * 1.5f);
        return RunicAltarEntity.RitualState.SUCCESS;
    }
}
