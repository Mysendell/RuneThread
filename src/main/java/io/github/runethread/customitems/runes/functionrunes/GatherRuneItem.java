package io.github.runethread.customitems.runes.functionrunes;

import io.github.runethread.customblocks.altar.RunicAltarEntity;
import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.util.AreaUtil;
import io.github.runethread.util.ILocation;
import io.github.runethread.util.InventoryUtil;
import io.github.runethread.util.ModifierMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class GatherRuneItem extends MainRuneItem{
    public GatherRuneItem(Properties properties, int cost, double scale, double scalingCost, double breakChance) {
        super(properties, cost, scale, scalingCost, breakChance);
    }

    @Override
    public RunicAltarEntity.RitualState perform(ServerLevel level, ServerPlayer player, ItemStack mainStack, double finalScale, ILocation destination, @Nullable ILocation reference, BlockPos origin, ModifierMap additionalData) {
        double radius = finalScale * ((MainRuneItem) mainStack.getItem()).getScale();
        AreaUtil.IteratePerimeterCircle(level, destination.getLocation(level), radius, (args) -> {
            ParticleOptions particleOptions = new DustParticleOptions(6, 2.0f);
            BlockPos pos = (BlockPos) args[0];
            level.sendParticles(particleOptions,
                    pos.getX() + 0.5,
                    pos.getY() + 0.1,
                    pos.getZ() + 0.5,
                    1,
                    0.0,
                    0.0,
                    0.0,
                    1.0);
        });
        AABB area = new AABB(destination.getLocation(level));
        area = area.inflate(radius); // TODO add some visual indication of area
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
