package io.github.runethread.customblocks;

import io.github.runethread.RuneThread;
import io.github.runethread.customblocks.altar.RunicAltarEntity;
import io.github.runethread.customblocks.craftingtable.animator.AnimatorEntity;
import io.github.runethread.customblocks.craftingtable.arcanetable.ArcaneTableEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CustomBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RuneThread.MODID);

    public static final Supplier<BlockEntityType<ArcaneTableEntity>> ARCANE_TABLE =
            BLOCK_ENTITY_TYPES.register("arcane_tablee", () -> new BlockEntityType<>(
                    ArcaneTableEntity::new, CustomBlocks.ARCANE_TABLE_BLOCK.get()));

    public static final Supplier<BlockEntityType<AnimatorEntity>> ANIMATOR =
            BLOCK_ENTITY_TYPES.register("animator", () -> new BlockEntityType<>(
                    AnimatorEntity::new, CustomBlocks.ANIMATOR_BLOCK.get()));

    public static final Supplier<BlockEntityType<RunicAltarEntity>> RUNIC_ALTAR =
            BLOCK_ENTITY_TYPES.register("runic_altar", () -> new BlockEntityType<>(
                    RunicAltarEntity::new, CustomBlocks.RUNIC_ALTAR_BLOCK.get()));
}