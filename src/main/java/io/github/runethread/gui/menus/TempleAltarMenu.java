package io.github.runethread.gui.menus;

import io.github.runethread.RuneThread;
import io.github.runethread.customTags.RuneTags;
import io.github.runethread.customblocks.altar.RunicAltarEntity;
import io.github.runethread.gui.CustomMenus;
import io.github.runethread.gui.Menu;
import io.github.runethread.gui.TagRestrictedSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class TempleAltarMenu extends Menu<RunicAltarEntity> {
    private IItemHandler mainRune;
    private IItemHandler destinationRune;
    private IItemHandler powerRune;
    private IItemHandler targetRune;

    public TempleAltarMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, playerInv.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public TempleAltarMenu(int id, Inventory playerInv, BlockEntity blockEntity) {
        super(id, playerInv, blockEntity, CustomMenus.TEMPLE_ALTAR_MENU.get());
        if (blockEntity instanceof RunicAltarEntity altarEntity) {
            mainRune = altarEntity.getMainRune();
            destinationRune = altarEntity.getDestinationRune();
            powerRune = altarEntity.getPower();
            targetRune = altarEntity.getTargetRune();
        } else {
            throw new IllegalArgumentException("Block entity must be an instance of RunicAltarEntity");
        }
        addPlayerInventory(playerInv, 32, 127, 18, 18);
        addPlayerHotbar(playerInv, 32, 185, 18);
        addRunicAltarSlots();
        TE_INVENTORY_SLOT_COUNT = 23;
    }

    private void addRunicAltarSlots() {
        for (int j = 0; j < 2; j++)
            for (int i = 0; i < 9; i++)
                this.addSlot(new SlotItemHandler(targetRune, i + j * 9, 32 + (i * 18), 18 + j * 70) {
                    @Override
                    public void set(ItemStack stack) {
                        super.set(stack);
                        if (!blockEntity.getLevel().isClientSide()) {
                            blockEntity.setChanged();
                        }
                    }
                    @Override
                    public void onTake(Player player, ItemStack stack) {
                        super.onTake(player, stack);
                        if (!blockEntity.getLevel().isClientSide()) {
                            blockEntity.setChanged();
                        }
                    }
                });

        this.addSlot(new TagRestrictedSlot(mainRune, 0, 104, 53, blockEntity,RuneTags.Items.FUNCTION_RUNE_TAG, RuneTags.Items.NATURE_RUNE_TAG));
        this.addSlot(new TagRestrictedSlot(destinationRune, 0, 50, 53, blockEntity, RuneTags.Items.LOCATON_RUNE_TAG));
        this.addSlot(new TagRestrictedSlot(destinationRune, 1, 68, 53, blockEntity, RuneTags.Items.LOCATON_RUNE_TAG));
        this.addSlot(new TagRestrictedSlot(powerRune, 0, 140, 53, blockEntity, RuneTags.Items.POWER_TAG));
        this.addSlot(new TagRestrictedSlot(powerRune, 1, 158, 53, blockEntity, RuneTags.Items.POWER_TAG));
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return ResourceLocation.fromNamespaceAndPath(RuneThread.MODID, "textures/gui/temple_altar.png");
    }
}
