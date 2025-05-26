package io.github.runethread.gui.menus;

import io.github.runethread.customblocks.craftingtable.arcanetable.ArcaneCraftResultSlot;
import io.github.runethread.customblocks.craftingtable.arcanetable.ArcaneTableEntity;
import io.github.runethread.gui.Menu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import static io.github.runethread.gui.CustomMenus.ARCANE_MENU;

public class ArcaneMenu extends Menu<ArcaneTableEntity> {
    private final IItemHandler inventory;
    private final IItemHandler output;

    public ArcaneMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, playerInv.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public ArcaneMenu(int id, Inventory playerInv, BlockEntity blockEntity) {
        super(id, playerInv, blockEntity, ARCANE_MENU.get());
        this.inventory = ((ArcaneTableEntity) blockEntity).getInventory();
        this.output  = ((ArcaneTableEntity) blockEntity).getOutput();

        addPlayerInventory(playerInv, 8, 84, 18, 18);
        addPlayerHotbar(playerInv, 8, 142, 18);
        addArcaneTableSlots();
    }

    private void addArcaneTableSlots() {
        this.addSlot(new ArcaneCraftResultSlot(blockEntity, player, level, 0, 124, 35));
        for (int row = 0, row2=0; row < 3; row++, row2 += 3) {
            for(int col = 0; col < 3; col++) {
                this.addSlot(new SlotItemHandler(inventory, row2 + col, 30 + row * 18, 17 + col * 18));
            }
        }
    }

    public ResourceLocation getBackgroundTexture() {
        return ResourceLocation.parse("runethread:textures/gui/arcane_table.png");
    }
}