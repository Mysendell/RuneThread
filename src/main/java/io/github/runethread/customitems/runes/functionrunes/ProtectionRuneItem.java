package io.github.runethread.customitems.runes.functionrunes;

import io.github.runethread.customblocks.altar.RunicAltarEntity;
import io.github.runethread.customeffects.CustomEffects;
import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.datacomponents.EntityData;
import io.github.runethread.util.Barrier;
import io.github.runethread.util.BarrierManager;
import io.github.runethread.util.ILocation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ProtectionRuneItem extends MainRuneItem{
    public ProtectionRuneItem(Properties properties, int cost, double scale, double scalingCost, double breakChance) {
        super(properties, cost, scale, scalingCost, breakChance);
    }

    @Override
    public RunicAltarEntity.RitualState perform(ServerLevel level, ServerPlayer player, ItemStack mainStack, double finalScale, ILocation destination, @Nullable ILocation reference, BlockPos origin, Map<String, Object> additionalData) {
        MainRuneItem item = (MainRuneItem) mainStack.getItem();
        int duration = (int) Math.round(20 * finalScale * item.getScale());
        if (destination instanceof EntityData entityData && entityData.entity(level) instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(CustomEffects.IMMUNITY, duration));
        } else {
            RunicAltarEntity runicAltarEntity = (RunicAltarEntity) level.getBlockEntity(origin);
            if(runicAltarEntity == null)
                return RunicAltarEntity.RitualState.ALMOST;

            Barrier barrier = new Barrier(
                    destination.getLocation(level),
                    item.getScale(),
                    runicAltarEntity,
                    duration
            );
            BarrierManager.addBarrier(barrier);
            runicAltarEntity.addBarrier(barrier);
        }
        return RunicAltarEntity.RitualState.SUCCESS;
    }
}
