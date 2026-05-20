package io.github.runethread.customblocks.structure;

import io.github.runethread.customblocks.CustomBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CollapseStructureEntity extends BlockEntity {
    public CollapseStructureEntity(BlockPos pos, BlockState blockState) {
        super(CustomBlockEntities.COLLAPSE_STRUCTURE.get(), pos, blockState);
    }
}
