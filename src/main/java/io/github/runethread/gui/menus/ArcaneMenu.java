package io.github.runethread.gui.menus;

import io.github.runethread.customblocks.craftingtable.arcanetable.ArcaneTableEntity;
import io.github.runethread.gui.AbstractMenu;
import io.github.runethread.gui.slots.CraftingOutputSlot;
import io.github.runethread.gui.slots.CraftingSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;

import static io.github.runethread.gui.CustomMenus.ARCANE_MENU;

public class ArcaneMenu extends AbstractMenu<ArcaneTableEntity> {
    private final IItemHandler inventory;

    public ArcaneMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, playerInv.player.level().getBlockEntity(buf.readBlockPos()));
        TE_INVENTORY_SLOT_COUNT = 10;
    }

    public ArcaneMenu(int id, Inventory playerInv, BlockEntity blockEntity) {
        super(id, playerInv, blockEntity, ARCANE_MENU.get());
        this.inventory = ((ArcaneTableEntity) blockEntity).getInput();

        addPlayerInventory(playerInv, 8, 84, 18, 18);
        addPlayerHotbar(playerInv, 8, 142, 18);
        addArcaneTableSlots();
    }

    private void addArcaneTableSlots() {
        this.addSlot(new CraftingOutputSlot(blockEntity.getOutput(), player, level, 0, 124, 35, blockEntity));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int index = row * 3 + col;
                int x = 30 + col * 18;
                int y = 17 + row * 18;
                this.addSlot(new CraftingSlot(inventory, index, x, y, blockEntity));
            }
        }
    }

    public ResourceLocation getBackgroundTexture() {
        return ResourceLocation.parse("runethread:textures/gui/arcane_table.png");
    }
}