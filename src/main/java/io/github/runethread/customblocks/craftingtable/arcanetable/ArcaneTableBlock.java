package io.github.runethread.customblocks.craftingtable.arcanetable;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArcaneTableBlock extends BaseEntityBlock {
    public static final MapCodec<ArcaneTableBlock> CODEC = simpleCodec(ArcaneTableBlock::new);
    public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class, Direction.Plane.HORIZONTAL);

    public ArcaneTableBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }


    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof ArcaneTableEntity arcaneTableEntity) {
            if (!level.isClientSide) {
                ((ServerPlayer) player).openMenu(new SimpleMenuProvider(arcaneTableEntity, Component.literal("Arcane Table")), pos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this));
            if (be instanceof ArcaneTableEntity arcaneTable) {
                System.out.println("Dropping ArcaneTable inventory at " + pos);
                for (int i = 0; i < arcaneTable.getInventory().getSlots(); i++) {
                    var stack = arcaneTable.getInventory().getStackInSlot(i);
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
