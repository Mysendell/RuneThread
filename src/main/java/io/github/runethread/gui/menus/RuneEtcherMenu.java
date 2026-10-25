package io.github.runethread.gui.menus;

import io.github.runethread.RuneThread;
import io.github.runethread.customblocks.etcher.RuneEtcherEntity;
import io.github.runethread.gui.AbstractMenu;
import io.github.runethread.gui.CustomMenus;
import io.github.runethread.gui.slots.CraftingSlot;
import io.github.runethread.gui.slots.OutputSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;

public class RuneEtcherMenu extends AbstractMenu<RuneEtcherEntity> {
    private final IItemHandler center;
    private final IItemHandler circle;

    public RuneEtcherMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, playerInv.player.level().getBlockEntity(buf.readBlockPos()));
        TE_INVENTORY_SLOT_COUNT = 9;
    }

    public RuneEtcherMenu(int id, Inventory playerInv, BlockEntity blockEntity) {
        super(id, playerInv, blockEntity, CustomMenus.RUNE_ETCHER_MENU.get());
        if(blockEntity instanceof RuneEtcherEntity etcherEntity) {
            this.center = etcherEntity.getCenter();
            this.circle = etcherEntity.getCircle();
        }
        else{
            throw new IllegalArgumentException("Block entity must be an instance of RuneEtcherEntity");
        }

        addPlayerHotbar(playerInv, 32, 185, 18);
        addPlayerInventory(playerInv, 32, 127, 18, 18);
        addEtcherSlots(playerInv);
    }

    private void addEtcherSlots(Inventory playerInv) {
        /*
            BIG 104 53
            104 15 Top
            134 24 Top Right
            143 53 Right
            133 82 Bottom Right
            104 91 Bottom
            75 82 Bottom Left
            66 53 Left
            75 24 Top Left
         */
        int[][] pos = {{104, 15}, {134, 24}, {143, 53}, {133, 82}, {104, 91}, {75, 82}, {66, 53}, {75, 24}};
        for(int i = 0; i < 8; i++){
            this.addSlot(new CraftingSlot(circle, i, pos[i][0], pos[i][1], blockEntity, playerInv));
        }
        this.addSlot(new OutputSlot(center, level, 0, 104, 53, blockEntity, true, playerInv));
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return ResourceLocation.fromNamespaceAndPath(RuneThread.MODID, "textures/gui/etcher_gui.png");
    }
}
