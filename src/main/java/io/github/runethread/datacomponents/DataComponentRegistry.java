package io.github.runethread.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.runethread.RuneThread;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
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
}

