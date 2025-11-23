package io.github.runethread.customblocks.structure;

import io.github.runethread.util.StructureUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public abstract class StructureCenterEntity extends BlockEntity {
    protected Set<BlockPos> structureBlocks = null;
    protected boolean isStructured = false;
    protected static StructureUtil.StructurePart[] structure;
    protected static int structureSize;

    public StructureCenterEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void createStructure(){
        if(!level.isClientSide)
            updateStructureState(isValidStructure());
    }

    public boolean isValidStructure() {
        structureBlocks = StructureUtil.ValidateAndAddStructure(structure, worldPosition, level, structureSize, this);
        return structureBlocks.size() == structureSize;
    }

    protected abstract void updateSelfState(boolean value);

    protected abstract void updateOtherStates(boolean value);

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

    public Set<BlockPos> getStructureBlocks() {
        return structureBlocks;
    }

    public void setStructureBlocks(Set<BlockPos> structureBlocks) {
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
