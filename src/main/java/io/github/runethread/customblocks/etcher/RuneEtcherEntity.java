package io.github.runethread.customblocks.etcher;

import io.github.runethread.customblocks.CustomBlockEntities;
import io.github.runethread.gui.menus.RuneEtcherMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RuneEtcherEntity extends BlockEntity implements MenuProvider {
    public RuneEtcherEntity(BlockPos pos, BlockState blockState) {
        super(CustomBlockEntities.RUNE_ETCHER.get(), pos, blockState);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.rune_etcher");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new RuneEtcherMenu(containerId, playerInventory, this);
    }

    public void onRemove() {
    }
}
