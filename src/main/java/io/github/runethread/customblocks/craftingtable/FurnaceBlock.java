package io.github.runethread.customblocks.craftingtable;

import com.mojang.serialization.MapCodec;
import io.github.runethread.customblocks.craftingtable.animator.Animator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public abstract class FurnaceBlock  extends BaseEntityBlock {
    public static final MapCodec<Animator> CODEC = simpleCodec(Animator::new);
    public static final BooleanProperty LIT = BooleanProperty.create("lit");


    public FurnaceBlock(Properties p_49224_) {
        super(p_49224_);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public abstract @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state);
}
