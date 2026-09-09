package io.github.runethread.customitems.runes;

import io.github.runethread.customitems.runes.functionrunes.RitualState;
import io.github.runethread.datacomponents.CompositeRuneData;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.util.ILocation;
import io.github.runethread.util.exceptions.CompositeRuneException;
import io.github.runethread.util.runeutils.ModifierMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CompositeRuneItem extends MainRuneItem{
    public CompositeRuneItem(Properties properties, int cost, double scale, double scalingCost, double breakChance) {
        super(properties, cost, scale, scalingCost, breakChance);
    }

    private CompositeRuneData getData(ItemStack compositeRuneItem){
        CompositeRuneData data = compositeRuneItem.get(DataComponentRegistry.COMPOSITE_RUNE);
        if(data == null) throw new CompositeRuneException("No main rune set");
        return data;
    }

    @Override
    public RitualState perform(ServerLevel level, ServerPlayer player, ItemStack mainStack, double finalScale, ILocation destination, @Nullable ILocation reference, BlockPos origin, ModifierMap additionalData) {
        CompositeRuneData data = getData(mainStack);
        additionalData.putAll(data.modifiers());
        return data.mainRune().perform(level, player, mainStack, finalScale, destination, reference, origin, additionalData);
    }

    @Override
    public int getCost(ItemStack compositeRuneItem) {
        CompositeRuneData data = getData(compositeRuneItem);
        return getCost() + data.mainRune().getCost();
    }

    @Override
    public double getScale(ItemStack compositeRuneItem) {
        CompositeRuneData data = getData(compositeRuneItem);
        return getScale() + data.mainRune().getScale();
    }

    @Override
    public double getScalingCost(ItemStack compositeRuneItem) {
        CompositeRuneData data = getData(compositeRuneItem);
        return getScalingCost() + data.mainRune().getScalingCost();
    }

    @Override
    public double getBreakChance(ItemStack compositeRuneItem) {
        CompositeRuneData data = getData(compositeRuneItem);
        return getScalingCost() + data.mainRune().getScalingCost();
    }

    @Override
    public boolean requiresReference(ItemStack compositeRuneItem) {
        CompositeRuneData data = getData(compositeRuneItem);
        return data.mainRune().requiresReference();
    }
}
