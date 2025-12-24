package io.github.runethread.customitems.runes.functionrunes;

import io.github.runethread.customblocks.altar.RunicAltarEntity;
import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.util.ChatUtils;
import io.github.runethread.util.ILocation;
import io.github.runethread.util.InventoryUtil;
import io.github.runethread.util.ModifierMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CreationRuneItem extends MainRuneItem{
    public CreationRuneItem(Properties properties, int cost, double scale, double scalingCost, double breakChance) {
        super(properties, cost, scale, scalingCost, breakChance);
    }

    @Override
    public RunicAltarEntity.RitualState perform(ServerLevel level, ServerPlayer player, ItemStack mainStack, double finalScale, ILocation destination, @Nullable ILocation reference, BlockPos origin, ModifierMap additionalData) {
        // TODO make cost same amount as max power rune
        if (!additionalData.containsKey("Collapse Rune")) {
            ChatUtils.sendErrorMessagePlayer(player.getName().getString(), "Creation rune requires a Collapse Rune to be used!", level);
            return RunicAltarEntity.RitualState.ALMOST;
        }
        ItemStack collapseStack = (ItemStack) additionalData.get("Collapse Rune");
        if (!collapseStack.has(DataComponentRegistry.STRUCTURE_DATA)) { // TODO change to item data Don't duplicate structures
            ChatUtils.sendErrorMessagePlayer(player.getName().getString(), "Creation rune requires a Collapse Rune with a valid structure data!", level);
            return RunicAltarEntity.RitualState.ALMOST;
        }
        ItemStack finalStack = collapseStack.copy();
        finalStack.setCount(1);
        InventoryUtil.dropStack(origin, level, finalStack);
        return RunicAltarEntity.RitualState.SUCCESS;
    }
}
