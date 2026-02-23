package io.github.runethread.customblocks.structure;

import io.github.runethread.util.Structure;
import io.github.runethread.util.StructureUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class StructureCenterEntity extends BlockEntity {
    protected Structure structureBlocks = null;
    protected boolean isStructured = false;
    protected static StructureUtil.StructurePart[] structure;
    protected static int structureSize;

    public StructureCenterEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void createStructure(){
        assert level != null;
        if(!level.isClientSide)
            updateStructureState(isValidStructure());
    }

    public boolean isValidStructure() {
        structureBlocks = StructureUtil.ValidateAndAddStructure(structure, worldPosition, level, structureSize);
        return structureBlocks.isValid();
    }

    protected abstract void updateSelfState(boolean value);

    protected void updateOtherStates(boolean value) {
        try{
            for (BlockPos pos : structureBlocks) {
                assert level != null;
                BlockState state = level.getBlockState(pos);
                if (state.getBlock() instanceof IStructurePart part)
                    part.setStructureCenter(this);
                level.setBlock(pos, state.setValue(IStructurePart.STRUCTURED, value), 2);
            }
        }
        catch (Exception e) {
            System.err.println("Error updating structure state: " + e.getMessage());
        }
    }

    protected void updateStructureState(boolean value){
        updateSelfState(value);
        updateOtherStates(value);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        createStructure();
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        isStructured = tag.getBoolean("Structured");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("Structured", isStructured);

    }

    public Structure getStructureBlocks() {
        return structureBlocks;
    }

    public void setStructureBlocks(Structure structureBlocks) {
        this.structureBlocks = structureBlocks;
    }

    public boolean isStructured() {
        return isStructured;
    }

    public void setStructured(boolean structured) {
        isStructured = structured;
    }

    public boolean getRemove(){
        return remove;
    }
}
