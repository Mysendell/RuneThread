package io.github.runethread.customitems.runes.functionrunes;

import io.github.runethread.customblocks.altar.RunicAltarEntity;
import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.StructureData;
import io.github.runethread.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CollapseRuneItem extends MainRuneItem {
    public CollapseRuneItem(Properties properties, int cost, int scale, double scalingCost, double breakChance) {
        super(properties, cost, scale, scalingCost, breakChance);
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

    @Override
    public RunicAltarEntity.RitualState perform(ServerLevel level, ServerPlayer player, ItemStack mainStack, double finalScale, ILocation destination, @Nullable ILocation reference, BlockPos origin, Map<String, Object> additionalData) {
        ListTag listTag = new ListTag();
        ItemStack finalStack = mainStack.copy();
        finalStack.setCount(1);
        mainStack.shrink(1);
        MainRuneItem item = (MainRuneItem) mainStack.getItem();
        AreaUtil.IterateAreaBox(level, destination.getLocation(level), finalScale * item.getScale(), (args) -> {
            BlockPos pos = (BlockPos) args[0];
            listTag.add(StructureUtil.serializeBlock(pos, destination.getLocation(level), level));
            level.removeBlock(pos, false);
        });
        CompoundTag radius = new CompoundTag(); // TODO save structure using structureData not saving every block
        radius.putDouble("radius", finalScale * item.getScale());
        listTag.addLast(radius);
        finalStack.set(DataComponentRegistry.STRUCTURE_DATA, new StructureData(listTag));
        finalStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        InventoryUtil.dropStack(origin, level, finalStack);
        return RunicAltarEntity.RitualState.SUCCESS;
    }
}
