package io.github.runethread.gui;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class TagRestrictedSlot extends SlotItemHandler {
    private final TagKey<Item>[] allowedTag;

    public TagRestrictedSlot(IItemHandler itemHandler, int slot, int x, int y, TagKey<Item>... allowedTag) {
        super(itemHandler, slot, x, y);
        this.allowedTag = allowedTag;
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
}