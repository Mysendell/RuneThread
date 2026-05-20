package io.github.runethread.customblocks.structure;

import com.mojang.serialization.MapCodec;
import io.github.runethread.customblocks.altar.RunicAltar;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CollapseStructureBlock extends BaseEntityBlock {
    private static final MapCodec<RunicAltar> CODEC = simpleCodec(RunicAltar::new);

    public CollapseStructureBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CollapseStructureEntity(pos, state);
    }
}
