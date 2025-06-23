package io.github.runethread.gui.menus;

import io.github.runethread.customTags.RuneTags;
import io.github.runethread.customblocks.craftingtable.altar.RunicAltarEntity;
import io.github.runethread.gui.CustomMenus;
import io.github.runethread.gui.Menu;
import io.github.runethread.gui.TagRestrictedSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;

public class RusticAltarMenu extends Menu<RunicAltarEntity> {
    private IItemHandler mainRune;
    private IItemHandler destinationRune;
    private IItemHandler powerRune;

    public RusticAltarMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, playerInv.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public RusticAltarMenu(int id, Inventory playerInv, BlockEntity blockEntity) {
        super(id, playerInv, blockEntity, CustomMenus.RUSTIC_ALTAR_MENU.get());
            mainRune = ((RunicAltarEntity) blockEntity).getMainRune();
            destinationRune = ((RunicAltarEntity) blockEntity).getDestinationRune();
            powerRune = ((RunicAltarEntity) blockEntity).getPower();

            addPlayerInventory(playerInv, 8, 84, 18, 18);
            addPlayerHotbar(playerInv, 8, 142, 18);
            addRunicAltarSlots();

            TE_INVENTORY_SLOT_COUNT = 3;
    }

    private void addRunicAltarSlots() {
        this.addSlot(new TagRestrictedSlot(mainRune, 0, 80, 31, RuneTags.Items.FUNCTION_RUNE_TAG, RuneTags.Items.NATURE_RUNE_TAG));
        this.addSlot(new TagRestrictedSlot(destinationRune, 0, 35, 31, RuneTags.Items.LOCATON_RUNE_TAG));
        this.addSlot(new TagRestrictedSlot(powerRune, 0, 125, 31, RuneTags.Items.POWER_TAG));
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return ResourceLocation.fromNamespaceAndPath("runethread", "textures/gui/archaic_altar.png");
    }
}
