package io.github.runethread.gui;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class TagRestrictedSlot extends SlotItemHandler {
    private final TagKey<Item>[] allowedTag;
    private final BlockEntity blockEntity;

    public TagRestrictedSlot(IItemHandler itemHandler, int slot, int x, int y,BlockEntity blockEntity ,TagKey<Item>... allowedTag) {
        super(itemHandler, slot, x, y);
        this.allowedTag = allowedTag;
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        for(TagKey<Item> allowedTag : this.allowedTag) {
            if (stack.is(allowedTag)) {
                return true;
            }
        }
        return false;
    }
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
}