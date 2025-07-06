package io.github.runethread.customblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StructurePartStairs extends StairBlock implements IStructurePart {
    protected StructureCenterEntity structureCenter;

    public StructurePartStairs(BlockState baseState, Properties properties) {
        super(baseState, properties);
        registerDefaultState(stateDefinition.any().setValue(STRUCTURED, false));
    }

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

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STRUCTURED);
    }

    @Override
    public @NotNull BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(STRUCTURED, false);
    }

    public StructureCenterEntity getStructureCenter() {
        return structureCenter;
    }
    public void setStructureCenter(StructureCenterEntity structureCenter) {
        this.structureCenter = structureCenter;
    }
}
