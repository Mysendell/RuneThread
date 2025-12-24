package io.github.runethread.customitems.runes.functionrunes;

import io.github.runethread.customblocks.altar.RunicAltarEntity;
import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.util.AreaUtil;
import io.github.runethread.util.ILocation;
import io.github.runethread.util.ModifierMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class LightningRuneItem extends MainRuneItem{
    public LightningRuneItem(Properties properties, int cost, double scale, double scalingCost, double breakChance) {
        super(properties, cost, scale, scalingCost, breakChance);
    }

    @Override
    public RunicAltarEntity.RitualState perform(ServerLevel level, ServerPlayer player, ItemStack mainStack, double finalScale, ILocation destination, @Nullable ILocation reference, BlockPos origin, ModifierMap additionalData) {
        double radiusScale, strengthScale;
        radiusScale = strengthScale = finalScale;
        MainRuneItem item = (MainRuneItem) mainStack.getItem();
        if (additionalData.containsKey("Scale Rune: MODIFIER")) {
            ItemStack scaleRune = (ItemStack) additionalData.get("Scale Rune: MODIFIER");
            double scale = scaleRune.get(DataComponentRegistry.SCALE_DATA).scale();
            if (scale < 0)
                scale = 1 / scale;
            scale = Math.abs(scale);
            radiusScale *= scale;
            strengthScale *= 1 / scale;
        }

        int radius = (int) Math.round(radiusScale / item.getScale());
        double strength = strengthScale % item.getScale();
        strength = strength == 0.0 ? 4.0 : strength;
        int finalStrength = (int) Math.pow(2, strength - 1);
        float damage = (float) (strengthScale / (item.getScale() * 2.0 * finalStrength));
        AreaUtil.IterateAreaCircle(level, destination.getLocation(level), radius, (args) -> {
            BlockPos pos = (BlockPos) args[0];
            for (int i = 0; i < finalStrength; i++)
                EntityType.LIGHTNING_BOLT.spawn(level, pos, EntitySpawnReason.TRIGGERED).setDamage(damage);
        });
        return RunicAltarEntity.RitualState.SUCCESS;
    }
}
