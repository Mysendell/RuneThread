package io.github.runethread.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtil {
    public static void dropStackHandler(BlockPos pos, Level level, IItemHandler stackHandler){
        for (int i = 0; i < stackHandler.getSlots(); i++) {
            ItemStack stack = stackHandler.extractItem(i, Integer.MAX_VALUE, false);
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
        }
    }

    public static void dropStack(BlockPos pos, Level level, ItemStack stack) {
        if (!stack.isEmpty()) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
        }
    }

    public static List<ItemStack> getCraftingItems(int width, int height, ItemStackHandler inventory) {
        List<ItemStack> items = new ArrayList<>(width * height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int idx = row * width + col;
                items.add(inventory.getStackInSlot(idx));
            }
        }
        return items;
    }

    public static CraftingInput getRecipeInput(int width, int height, List<ItemStack> items) {
        return CraftingInput.of(width, height, items);
    }

    public static boolean canFullyOutput(List<ItemStack> outputStacks, ItemStack result) {
        int remaining = result.getCount();

        for (ItemStack outputStack : outputStacks) {
            if (remaining <= 0) break;

            if (outputStack.isEmpty()) {
                int max = result.getMaxStackSize();
                int canInsert = Math.min(max, remaining);
                remaining -= canInsert;
            } else if (ItemStack.isSameItemSameComponents(outputStack, result)) {
                int space = outputStack.getMaxStackSize() - outputStack.getCount();
                int canInsert = Math.min(space, remaining);
                remaining -= canInsert;
            }
        }
        return remaining <= 0;
    }

    public static void removeCraftingItems(int width, int height, ItemStackHandler inventory, int[] countMap, BlockPos pos, Level level) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int idx = row * width + col;
                int toRemove = countMap[idx];
                if (toRemove == 0) continue;
                ItemStack item = inventory.extractItem(idx, toRemove, false);

                ItemStack remainder = item.getCraftingRemainder();
                if (!remainder.isEmpty()) addItemStackInventory(level, pos, inventory, remainder);
            }
        }
    }

    /**
     * Adds an ItemStack to an inventory, returning any remainder that could not be added.
     * @param inventory The inventory to add to.
     * @param stackToAdd The ItemStack to add.
     * @return The remainder ItemStack that could not be added.
     *
     * @see #addItemStackInventory(Level, BlockPos, IItemHandler, ItemStack)
     */
    public static ItemStack addItemStackInventory(IItemHandler inventory, ItemStack stackToAdd) {
        ItemStack itemStack = stackToAdd.copy();
        for (int j=0; j < inventory.getSlots(); j++)
            itemStack = inventory.insertItem(j, itemStack, false);
        return itemStack;
    }

    /**
     * Adds an ItemStack to an inventory, dropping any remainder into the world at the given position.
     * @param level The level to drop remainder in.
     * @param pos The position to drop remainder at.
     * @param inventory The inventory to add to.
     * @param stackToAdd The ItemStack to add.
     *
     * @see #addItemStackInventory(IItemHandler, ItemStack)
     */
    public static void addItemStackInventory(Level level, BlockPos pos, IItemHandler inventory, ItemStack stackToAdd) {
        ItemStack itemStack = addItemStackInventory(inventory, stackToAdd);
        if(!itemStack.isEmpty())
            dropStack(pos, level, itemStack);
    }

    public static void addItemHandlerInventory(Level level, BlockPos pos, IItemHandler inventory, IItemHandler stacksToAdd) {
        for(int i=0; i < stacksToAdd.getSlots(); i++) {
            ItemStack itemStack = stacksToAdd.extractItem(i, Integer.MAX_VALUE, false);
            for (int j=0; j < inventory.getSlots(); j++)
                itemStack = inventory.insertItem(j, itemStack, false);

            if(!itemStack.isEmpty())
                dropStack(pos, level, itemStack);
        }
    }

    public static void addItemStackHandlerToBlock(Level level, BlockPos pos, IItemHandler stacksToAdd) {
        IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);

        if (itemHandler == null)
            dropStackHandler(pos, level, stacksToAdd);
        else
            addItemHandlerInventory(level, pos, itemHandler, stacksToAdd);
    }
}
