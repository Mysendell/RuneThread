package io.github.runethread.customitems.runes.functionrunes;

import io.github.runethread.customblocks.altar.RunicAltarEntity;
import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.util.ILocation;
import io.github.runethread.util.InventoryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class GatherRuneItem extends MainRuneItem{
    public GatherRuneItem(Properties properties, int cost, double scale, double scalingCost, double breakChance) {
        super(properties, cost, scale, scalingCost, breakChance);
    }

    @Override
    public RunicAltarEntity.RitualState perform(ServerLevel level, ServerPlayer player, ItemStack mainStack, double finalScale, ILocation destination, @Nullable ILocation reference, BlockPos origin, Map<String, Object> additionalData) {
        AABB area = new AABB(destination.getLocation(level));
        area = area.inflate(finalScale * ((MainRuneItem) mainStack.getItem()).getScale()); // TODO add some visual indication of area
        NonNullList<ItemStack> items = NonNullList.create();
        for (Entity entity : level.getEntities(null, area)) {
            if (entity instanceof ItemEntity itemEntity) {
                ItemStack itemStack = itemEntity.getItem();
                items.add(itemStack);
                entity.discard();
            }
        }
        BlockPos destinationBlock = reference.getLocation(level);
        InventoryUtil.addItemStackHandlerToBlock(level, destinationBlock, new ItemStackHandler(items));
        return RunicAltarEntity.RitualState.SUCCESS;
    }

    @Override
    public boolean requiresReference() {
        return true;
    }
}
