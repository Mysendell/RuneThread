package io.github.runethread.customblocks;

import io.github.runethread.RuneThread;
import io.github.runethread.customblocks.altar.RunicAltarEntity;
import io.github.runethread.customblocks.craftingtable.animator.AnimatorCraftingEntity;
import io.github.runethread.customblocks.craftingtable.arcanetable.ArcaneTableEntity;
import io.github.runethread.customblocks.etcher.RuneEtcherEntity;
import io.github.runethread.customblocks.structure.CollapseStructureEntity;
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

    public static final Supplier<BlockEntityType<AnimatorCraftingEntity>> ANIMATOR =
            BLOCK_ENTITY_TYPES.register("animator", () -> new BlockEntityType<>(
                    AnimatorCraftingEntity::new, CustomBlocks.ANIMATOR_BLOCK.get()));

    public static final Supplier<BlockEntityType<RunicAltarEntity>> RUNIC_ALTAR =
            BLOCK_ENTITY_TYPES.register("runic_altar", () -> new BlockEntityType<>(
                    RunicAltarEntity::new, CustomBlocks.RUNIC_ALTAR_BLOCK.get()));

    public static final Supplier<BlockEntityType<RuneEtcherEntity>> RUNE_ETCHER =
            BLOCK_ENTITY_TYPES.register("rune_etcher", () -> new BlockEntityType<>(
                    RuneEtcherEntity::new, CustomBlocks.RUNE_ETCHER.get()));

    public static final Supplier<BlockEntityType<CollapseStructureEntity>> COLLAPSE_STRUCTURE =
            BLOCK_ENTITY_TYPES.register("collapse_structure", () -> new BlockEntityType<>(
                    CollapseStructureEntity::new, CustomBlocks.COLLAPSE_STRUCTURE.get()));
}