package io.github.runethread.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public interface ILocation {
    BlockPos getLocation(Level level);
    void addToTooltip(Item.TooltipContext ctx, Consumer<Component> tooltipAdder, TooltipFlag flag);
}
