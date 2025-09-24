package io.github.runethread.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.runethread.RuneThread;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DataComponentRegistry {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, RuneThread.MODID);

    public static final Codec<LocationData> LOCATION_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("posX").forGetter(LocationData::posX),
                    Codec.INT.fieldOf("posY").forGetter(LocationData::posY),
                    Codec.INT.fieldOf("posZ").forGetter(LocationData::posZ)
            ).apply(instance, LocationData::new)
    );
    public static final Supplier<DataComponentType<LocationData>> LOCATION_DATA =
            DATA_COMPONENTS.registerComponentType(
                    "location_data",
                    builder -> builder.persistent(LOCATION_CODEC)
            );

    public static final Codec<EntityData> ENTITY_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("UUID").forGetter(EntityData::UUID),
                    Codec.STRING.fieldOf("name").forGetter(EntityData::name)
            ).apply(instance, EntityData::new)
    );
    public static final Supplier<DataComponentType<EntityData>> ENTITY_DATA =
            DATA_COMPONENTS.registerComponentType(
                    "entity_data",
                    builder -> builder.persistent(ENTITY_CODEC)
            );

    public static final Supplier<DataComponentType<PowerData>> POWER_DATA =
            DATA_COMPONENTS.registerComponentType(
                    "power_value",
                    builder -> builder.persistent(RecordCodecBuilder.create(instance ->
                            instance.group(
                                    Codec.INT.fieldOf("power").forGetter(PowerData::power)
                            ).apply(instance, PowerData::new)
                    ))
            );

    public static final Supplier<DataComponentType<ScaleData>> SCALE_DATA =
            DATA_COMPONENTS.registerComponentType(
                    "scale_value",
                    builder -> builder.persistent(RecordCodecBuilder.create(instance ->
                            instance.group(
                                    Codec.DOUBLE.fieldOf("scale").forGetter(ScaleData::scale),
                                    Codec.STRING.fieldOf("mode").forGetter(scaleData -> scaleData.mode().name())
                            ).apply(instance, (scale, mode) -> new ScaleData(scale, ScaleData.Mode.valueOf(mode)))
                    ))
            );

    public static final Supplier<DataComponentType<IndicatorData>> RITUAL_STATE =
            DATA_COMPONENTS.registerComponentType(
                    "ritual_state",
                    builder -> builder.persistent(RecordCodecBuilder.create(instance ->
                            instance.group(
                                    Codec.STRING.fieldOf("state").forGetter(IndicatorData::ritualState)
                            ).apply(instance, IndicatorData::new)
                    ))
            );

    public static final Codec<ListTag> LIST_TAG_CODEC = Codec.PASSTHROUGH.comapFlatMap(
            dynamic -> {
                Tag tag = dynamic.convert(NbtOps.INSTANCE).getValue();
                if (tag instanceof ListTag listTag) {
                    return DataResult.success(listTag);
                }
                return DataResult.error(() -> "Not a ListTag");
            },
            listTag -> new Dynamic<>(NbtOps.INSTANCE, listTag)
    );

    public static final Supplier<DataComponentType<StructureData>> STRUCTURE_DATA =
            DATA_COMPONENTS.registerComponentType(
                    "structure_data",
                    builder -> builder.persistent(RecordCodecBuilder.create(instance ->
                            instance.group(
                                    LIST_TAG_CODEC.fieldOf("structure_blocks").forGetter(StructureData::structure)
                            ).apply(instance, StructureData::new)
                    ))
            );
}

