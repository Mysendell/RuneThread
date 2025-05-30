package io.github.runethread.customentities;

import io.github.runethread.customentities.cakegolem.CakeGolem;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion.MOD_ID;

public class customEntities {
    public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(MOD_ID);

    public static final Supplier<EntityType<CakeGolem>> CAKE_GOLEM = ENTITY_TYPES.register(
            "cake_golem",
            () -> EntityType.Builder.of(
                            CakeGolem::new,
                            MobCategory.CREATURE
                    )
                    .sized(1.625F, 1.875F)
                    .eyeHeight(1.4375f)
                    .build(ResourceKey.create(
                            Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(MOD_ID, "cake_golem")
                    ))
    );
    public static final ModelLayerLocation CAKE_GOLEM_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "cake_golem"),
            "main"
    );
}
