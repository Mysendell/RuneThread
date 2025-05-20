package io.github.runethread.customblocks.craftingtable;

import io.github.runethread.menus.ArcaneMenu;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import static io.github.runethread.customblocks.CustomBlockEntities.ARCANE_TABLE;
import static io.github.runethread.customblocks.CustomBlocks.ARCANE_TABLE_BLOCK;

public class ArcaneTableEntity extends BlockEntity implements MenuProvider{
    public static final ItemStackHandler inventory = new ItemStackHandler(9);

    public ArcaneTableEntity(BlockPos pos, BlockState state) {
        super(ARCANE_TABLE.get(), pos, state);
    }

    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof ArcaneTableEntity myBe) {
            return new SimpleMenuProvider(
                    (windowId, inv, player) -> new ArcaneMenu(windowId, inv, new FriendlyByteBuf(Unpooled.buffer())),
                    Component.translatable("container.magic_table")
            );
        }
        return null;
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        player.openMenu(getMenuProvider(state, level, pos));
        return InteractionResult.SUCCESS;
    }

    @Override
    public Component getDisplayName() {
        return null;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return null;
    }
}
