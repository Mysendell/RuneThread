package io.github.runethread.gui.slots;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * A slot that restricts item placement based on item tags.<br>
 * Can operate in whitelist or blacklist mode, and supports strict mode for tag matching.<br>
 * In strict mode <b>all</b> tags must match, otherwise <b>any</b> tag match is sufficient.
 * @see ISlotType
 */
public class TagRestrictedSlot extends SlotItemHandler implements  ISlotType {
    protected final Set<TagKey<Item>> filteredTags;
    private final boolean whitelist;
    private final boolean strictMode;
    private final BlockEntity blockEntity;

    /**
     * Creates a new TagRestrictedSlot.
     * @param itemHandler the item handler assigned to this slot
     * @param slotIndex the slot index inside that item handler
     * @param x X position on the GUI
     * @param y Y position on the GUI
     * @param blockEntity the block entity this slot belongs to
     * @param whitelist if true, only items matching the tags are allowed; if false, items matching the tags are disallowed
     * @param strictMode if true, all tags must match for an item to be considered a match; if false, any tag match is sufficient
     * @param filteredTags the set of tags to filter items by
     */
    public TagRestrictedSlot(IItemHandler itemHandler, int slotIndex, int x, int y, BlockEntity blockEntity, boolean whitelist, boolean strictMode, Set<TagKey<Item>> filteredTags) {
        super(itemHandler, slotIndex, x, y);
        this.filteredTags = filteredTags;
        this.blockEntity = blockEntity;
        this.whitelist = whitelist;
        this.strictMode = strictMode;
    }


    /**
     * Checks if the given ItemStack can be placed in this slot based on its tags and the slot's configuration.
     * @param stack the ItemStack to check
     * @return true if the item can be placed in the slot, false otherwise
     */
    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        boolean matches;
        if(strictMode)
            matches = stack.getTags().allMatch(filteredTags::contains);
        else
            matches = stack.getTags().anyMatch(filteredTags::contains);

        return matches == whitelist;
    }

    @Override
    public void set(@NotNull ItemStack stack) {
        super.set(stack);
        assert blockEntity.getLevel() != null;
        if (!blockEntity.getLevel().isClientSide()) {
            blockEntity.setChanged();
        }
    }

    @Override
    public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        super.onTake(player, stack);
        assert blockEntity.getLevel() != null;
        if (!blockEntity.getLevel().isClientSide()) {
            blockEntity.setChanged();
        }
    }
}