package io.github.runethread.customblocks.craftingtable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ArcaneTableBlock extends Block implements EntityBlock {

    public ArcaneTableBlock(Properties p_49795_) {
        super(p_49795_);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        System.out.println("ArcaneTableBlock#use called");
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ArcaneTableEntity arcaneTable) {
                player.openMenu(arcaneTable);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ArcaneTableEntity arcaneTable) {
                System.out.println("Dropping ArcaneTable inventory at " + pos);
                for (int i = 0; i < arcaneTable.getInventory().getSlots(); i++) {
                    var stack = arcaneTable.getInventory().getStackInSlot(i);
                    System.out.println("Slot " + i + ": " + stack);
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                }
            } else {
                System.out.println("No ArcaneTableEntity at " + pos);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ArcaneTableEntity(pos, state);
    }
}
