package io.github.runethread.menus;

import io.github.runethread.customblocks.craftingtable.ArcaneTableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class ArcaneMenu extends AbstractContainerMenu {
    private final BlockEntity blockEntity;
    private final IItemHandler inventory;

    public ArcaneMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        super(CustomMenus.ARCANE_MENU.get(), id);
        BlockPos pos = buf.readBlockPos();
        this.blockEntity = (ArcaneTableEntity) playerInv.player.level().getBlockEntity(pos);
        this.inventory = ArcaneTableEntity.inventory; // Access via a getter method
        this.addSlot(new SlotItemHandler(inventory, 0, 30, 17));
        this.addSlot(new SlotItemHandler(inventory, 1, 124, 35));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    public ResourceLocation getBackgroundTexture() {
        return ResourceLocation.parse("mymod:textures/gui/my_gui.png");

    }
}
