package io.github.runethread.customblocks;

import io.github.runethread.RuneThread;
import io.github.runethread.customblocks.craftingtable.ArcaneTableEntity;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.Set;

public class CustomBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RuneThread.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ArcaneTableEntity>> ARCANE_TABLE =
            BLOCK_ENTITY_TYPES.register("arcane_table",
                    () -> new BlockEntityType<>(
                            ArcaneTableEntity::new,
                            CustomBlocks.ARCANE_TABLE_BLOCK.get()
                    ));
}
