package io.github.runethread.customitems.runes;

import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.util.StructureUtil;
import io.github.runethread.util.TeleportUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class CollapseRuneItem extends MainRuneItem {
    public CollapseRuneItem(Properties properties, int cost, int scale, double scalingCost, double breakChance, RuneFunction runeFunction) {
        super(properties, cost, scale, scalingCost, breakChance, runeFunction);
    }

    // TODO check colors
    // TODO Add reverse to remove air blocks
    // TODO Add direction arrow support (center not being actual center)

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide())
            return InteractionResult.PASS;
        Player player = context.getPlayer();
        if (!player.isCrouching())
            return InteractionResult.PASS;
        BlockPos center = context.getClickedPos();
        ListTag listTag;
        try {
            listTag = stack.get(DataComponentRegistry.STRUCTURE_DATA.get()).structure();
        }
        catch (NullPointerException e) {
            return InteractionResult.PASS;
        }

        double radius = ((CompoundTag) listTag.getLast()).getDouble("radius") + 1;
        StructureUtil.placeStructureViaTag(listTag, center, level);
        stack.shrink(1);
        player.setItemInHand(context.getHand(), stack);
        TeleportUtil.teleportManager(center, player, (ServerLevel) level, radius, radius);
        return InteractionResult.SUCCESS;
    }
}
