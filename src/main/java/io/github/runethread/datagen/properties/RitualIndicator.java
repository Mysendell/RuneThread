package io.github.runethread.datagen.properties;

import com.mojang.serialization.MapCodec;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.IndicatorData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public record RitualIndicator() implements RangeSelectItemModelProperty {
    public static final MapCodec<RitualIndicator> MAP_CODEC = MapCodec.unit(new RitualIndicator());

    @Override
    public float get(ItemStack stack, ClientLevel level, LivingEntity entity, int seed) {
        IndicatorData indicatorData = new IndicatorData("NEUTRAL");
        return switch (stack.getOrDefault(DataComponentRegistry.RITUAL_STATE.get(), indicatorData).ritualState()) {
            case "SUCCESS" -> 1.0f;
            case "FAIL" -> 2.0f;
            case "ALMOST" -> 3.0f;
            default -> 0.0f;
        };
    }

    @Override
    public MapCodec<? extends RangeSelectItemModelProperty> type() {
        return MAP_CODEC;
    }
}
