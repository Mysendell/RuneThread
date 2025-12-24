package io.github.runethread.customitems.runes.functionrunes;

import io.github.runethread.customblocks.altar.RunicAltarEntity;
import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.util.AreaUtil;
import io.github.runethread.util.ILocation;
import io.github.runethread.util.ModifierMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FireRuneItem extends MainRuneItem{
    public FireRuneItem(Properties properties, int cost, double scale, double scalingCost, double breakChance) {
        super(properties, cost, scale, scalingCost, breakChance);
    }

    @Override
    public RunicAltarEntity.RitualState perform(ServerLevel level, ServerPlayer player, ItemStack mainStack, double finalScale, ILocation destination, @Nullable ILocation reference, BlockPos origin, ModifierMap additionalData) {
        double radius = finalScale * ((MainRuneItem) mainStack.getItem()).getScale();
        BlockPos center = destination.getLocation(level);
        center = center.offset(0, 1, 0);
        AreaUtil.IterateAreaSphere(level, center, radius, (args) -> {
            BlockPos pos = (BlockPos) args[0];
            if (BaseFireBlock.canBePlacedAt(level, pos, Direction.DOWN)) {
                BlockState blockstate = BaseFireBlock.getState(level, pos);
                level.setBlockAndUpdate(pos, blockstate);
            }
        });
        return RunicAltarEntity.RitualState.SUCCESS;
    }
}
