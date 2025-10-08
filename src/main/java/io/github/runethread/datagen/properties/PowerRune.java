package io.github.runethread.datagen.properties;

import com.mojang.serialization.MapCodec;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.PowerData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record  PowerRune() implements RangeSelectItemModelProperty {
    public static MapCodec<PowerRune> MAP_CODEC  = MapCodec.unit(new PowerRune());

    @Override
    public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        return stack.getOrDefault(DataComponentRegistry.POWER_DATA.get(), new PowerData(0)).power();
    }

    @Override
    public MapCodec<? extends RangeSelectItemModelProperty> type() {
        return MAP_CODEC;
    }

}
