package io.github.runethread.customblocks.structure;

import io.github.runethread.util.structure.StructureUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class StructureCenterEntity extends BlockEntity {
    protected boolean isStructured = false;
    protected static String structureName;

    public StructureCenterEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void createStructure(){
        assert level != null;
        if(!level.isClientSide)
            updateSelfState(isValidStructure());
    }

    public boolean isValidStructure() {
        return StructureUtil.ValidateStructure(structureName, this, (ServerLevel) level);
    }

    protected abstract void updateSelfState(boolean value);

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
