package io.github.runethread.gui.menus;

import io.github.runethread.recipes.CraftingOutputSlot;
import io.github.runethread.customblocks.craftingtable.arcanetable.ArcaneTableEntity;
import io.github.runethread.gui.Menu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import static io.github.runethread.gui.CustomMenus.ARCANE_MENU;

public class ArcaneMenu extends Menu<ArcaneTableEntity> {
    private final IItemHandler inventory;

    public ArcaneMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, playerInv.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public ArcaneMenu(int id, Inventory playerInv, BlockEntity blockEntity) {
        super(id, playerInv, blockEntity, ARCANE_MENU.get());
        this.inventory = ((ArcaneTableEntity) blockEntity).getInventory();

        addPlayerInventory(playerInv, 8, 84, 18, 18);
        addPlayerHotbar(playerInv, 8, 142, 18);
        addArcaneTableSlots();
    }

    private void addArcaneTableSlots() {
        this.addSlot(new CraftingOutputSlot(blockEntity, player, level, 0, 124, 35));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int index = row * 3 + col;
                int x = 30 + col * 18;
                int y = 17 + row * 18;
                this.addSlot(new SlotItemHandler(inventory, index, x, y) {
                    @Override
                    public void set(ItemStack stack) {
                        super.set(stack);
                        if (!blockEntity.getLevel().isClientSide()) {
                            blockEntity.scheduleCraftingUpdate();
                        }
                    }
                    @Override
                    public void onTake(Player player, ItemStack stack) {
                        super.onTake(player, stack);
                        if (!blockEntity.getLevel().isClientSide()) {
                            blockEntity.scheduleCraftingUpdate();
                        }
                    }
                });
            }
        }
    }

    public ResourceLocation getBackgroundTexture() {
        return ResourceLocation.parse("runethread:textures/gui/arcane_table.png");
    }
}