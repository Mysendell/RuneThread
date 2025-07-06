package io.github.runethread.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtil {
    public static void dropStackHandler(BlockPos pos, Level level, ItemStackHandler stackHandler){
        for (int i = 0; i < stackHandler.getSlots(); i++) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stackHandler.getStackInSlot(i));
            stackHandler.setStackInSlot(i, ItemStack.EMPTY);
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
                // Can insert up to the max stack size
                int max = result.getMaxStackSize();
                int canInsert = Math.min(max, remaining);
                remaining -= canInsert;
            } else if (ItemStack.isSameItemSameComponents(outputStack, result)) {
                // Can fill up to max stack size
                int space = outputStack.getMaxStackSize() - outputStack.getCount();
                int canInsert = Math.min(space, remaining);
                remaining -= canInsert;
            }
        }
        return remaining <= 0;
    }

    public static void distributeOutput(ItemStackHandler output, ItemStack result) {
        int toInsert = result.getCount();
        ItemStack toDistribute = result.copy();

        for (int i = 0; i < output.getSlots() && toInsert > 0; i++) {
            // Prepare stack to try to insert
            ItemStack attempt = toDistribute.copy();
            attempt.setCount(toInsert);

            ItemStack remainder = output.insertItem(i, attempt, false);

            // Update how much is left to insert
            int inserted = attempt.getCount() - (remainder.isEmpty() ? 0 : remainder.getCount());
            toInsert -= inserted;
        }
    }

    public static void removeCraftingItems(int width, int height, ItemStackHandler inventory) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int idx = row * width + col;
                ItemStack item = inventory.extractItem(idx, 1, false);
                ItemStack remainder = item.getCraftingRemainder();
                if (!remainder.isEmpty()) {
                    inventory.insertItem(idx, remainder, false);
                }
            }
        }
    }
}
