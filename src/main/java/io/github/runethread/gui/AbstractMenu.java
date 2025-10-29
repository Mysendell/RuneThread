package io.github.runethread.gui;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;


/**
 * Base class for all menus
 * Defines common player inventory slot creation methods and quick move logic
 * @param <T> BlockEntity type
 */
@SuppressWarnings("unchecked")
public abstract class AbstractMenu<T extends BlockEntity> extends AbstractContainerMenu {
    protected final T blockEntity;
    protected final Level level;
    protected final Player player;


    /** @param id        Window id
     * @param playerInv Player inventory
     * @param blockEntity  Block entity instance
     * @param Menu      Menu type
     */
    public AbstractMenu(int id, Inventory playerInv, BlockEntity blockEntity, MenuType<?> Menu) {
        super(Menu, id);
        this.blockEntity = (T) blockEntity;
        this.level = playerInv.player.level();
        this.player = playerInv.player;
    }

    /**
     * Adds the player inventory slots to the menu
     * @param playerInv player inventory instance
     * @param startX starting X position
     * @param startY starting Y position
     * @param spaceX spacing between slots in X direction
     * @param spaceY spacing between slots in Y direction
     */
    protected void addPlayerInventory(Inventory playerInv, int startX, int startY, int spaceX, int spaceY) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, startX + col * spaceX, startY + row * spaceY));
            }
        }
    }

    /**
     * Adds the player hotbar slots to the menu
     * @param playerInv player inventory instance
     * @param startX starting X position
     * @param startY starting Y position
     * @param spaceX spacing between slots in X direction
     */
    protected void addPlayerHotbar(Inventory playerInv, int startX, int startY, int spaceX) {
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInv, col, startX + col * spaceX, startY));
        }
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    /**
     * Number of slots in the block entity's inventory that are quick move valid
     */
    protected static int TE_INVENTORY_SLOT_COUNT = 9;

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return AbstractContainerMenu.stillValid(
                ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player,
                blockEntity.getBlockState().getBlock()
        );
    }

    public abstract ResourceLocation getBackgroundTexture();
}
