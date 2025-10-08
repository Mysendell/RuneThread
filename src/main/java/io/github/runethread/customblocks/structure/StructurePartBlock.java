package io.github.runethread.customblocks.structure;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StructurePartBlock extends BaseEntityBlock implements IStructurePart{
    public static final MapCodec<StructurePartBlock> CODEC = simpleCodec(StructurePartBlock::new);

    protected StructureCenterEntity structureCenter;

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        if(structureCenter != null)
            structureCenter.createStructure();
        else if (neighborBlock instanceof IStructurePart structurePart){
            StructureCenterEntity center = structurePart.getStructureCenter();
            if (center != null)
                center.createStructure();
        }
        super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
    }

    public StructurePartBlock(Properties p_49224_) {
        super(p_49224_);
        registerDefaultState(stateDefinition.any().setValue(STRUCTURED, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STRUCTURED);
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(STRUCTURED, false);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public StructureCenterEntity getStructureCenter() {
        return structureCenter;
    }
    public void setStructureCenter(StructureCenterEntity structureCenter) {
        this.structureCenter = structureCenter;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
