package io.github.runethread.customblocks.craftingtable;

import io.github.runethread.recipes.smelting.Philosophal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public abstract class PhilosphalFurnaceEntity<J extends FurnaceBlock, T extends Philosophal> extends FurnaceEntity<T, J> {
    protected ItemStackHandler lifeEnergy = new ItemStackHandler(1);

    public PhilosphalFurnaceEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public ItemStackHandler getLifeEnergy() {
        return lifeEnergy;
    }
}
