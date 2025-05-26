package io.github.runethread.customblocks;

import io.github.runethread.RuneThread;
import io.github.runethread.customblocks.craftingtable.arcanetable.ArcaneTableEntity;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class CustomBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RuneThread.MODID);

    public static final Supplier<BlockEntityType<ArcaneTableEntity>> ARCANE_TABLE =
            BLOCK_ENTITY_TYPES.register("arcane_tablee", () -> new BlockEntityType<>(
                    ArcaneTableEntity::new, CustomBlocks.ARCANE_TABLE_BLOCK.get()));
}