package io.github.runethread.customblocks.craftingtable;

import io.github.runethread.util.InventoryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public abstract class PhilosphalFurnaceCraftingEntity extends FurnaceCraftingEntity {
    protected ItemStackHandler lifeEnergy = new ItemStackHandler(1);

    public PhilosphalFurnaceCraftingEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public ItemStackHandler getLifeEnergy() {
        return lifeEnergy;
    }

    public void onRemove(){
        super.onRemove();
        InventoryUtil.dropStackHandler(worldPosition, level, lifeEnergy);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("lifeEnergy", lifeEnergy.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("lifeEnergy", Tag.TAG_COMPOUND)) {
            lifeEnergy.deserializeNBT(registries, tag.getCompound("lifeEnergy"));
        }
    }
}
