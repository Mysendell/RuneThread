package io.github.runethread.customblocks.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class StructureCenterBlock extends BaseEntityBlock{
    public static BooleanProperty STRUCTURED = BooleanProperty.create("structured");
    protected StructureCenterEntity structureCenter;

    public StructureCenterBlock(Properties p_49224_) {
        super(p_49224_);
        registerDefaultState(stateDefinition.any().setValue(STRUCTURED, false));
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        if (level.getBlockEntity(pos) instanceof StructureCenterEntity Entity && !level.isClientSide)
            Entity.createStructure();
        super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STRUCTURED);
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(STRUCTURED, false);
    }
}
