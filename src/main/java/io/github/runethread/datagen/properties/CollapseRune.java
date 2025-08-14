package io.github.runethread.datagen.properties;

import com.mojang.serialization.MapCodec;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.StructureData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record CollapseRune() implements RangeSelectItemModelProperty {
    public static final MapCodec<CollapseRune> MAP_CODEC  = MapCodec.unit(new CollapseRune());

    @Override
    public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        StructureData structureData = stack.get(DataComponentRegistry.STRUCTURE_DATA.get());
        return structureData == null ? 1.0f : 2.0f;
    }

    @Override
    public MapCodec<? extends RangeSelectItemModelProperty> type() {
        return MAP_CODEC;
    }
}
