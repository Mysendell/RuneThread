package io.github.runethread.gui.menus;

import io.github.runethread.RuneThread;
import io.github.runethread.customblocks.etcher.RuneEtcherEntity;
import io.github.runethread.gui.AbstractMenu;
import io.github.runethread.gui.CustomMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RuneEtcherMenu extends AbstractMenu<RuneEtcherEntity> {

    public RuneEtcherMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, playerInv.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public RuneEtcherMenu(int id, Inventory playerInv, BlockEntity blockEntity) {
        super(id, playerInv, blockEntity, CustomMenus.RUNE_ETCHER_MENU.get());

        addPlayerInventory(playerInv, 32, 127, 18, 18);
        addPlayerHotbar(playerInv, 32, 185, 18);
    }

    private void addEtcherSlots(){
        /*
            BIG 104 53
            104 15 Top
            133 24 Top Right
            143 53 Right
            133 82 Bottom Right
            104 91 Bottom
            75 82 Bottom Left
            66 53 Left
            75 24 Top Left
         */
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return ResourceLocation.fromNamespaceAndPath(RuneThread.MODID, "textures/gui/etcher_gui.png");
    }
}
