package io.github.runethread.datacomponents;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;

public record PowerData(int power) {
    public void addToTooltip(Item.TooltipContext ctx, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.literal("Power: " + getRealPower()).withStyle(ChatFormatting.BLUE));
    }
    public double getRealPower() {
        return new double[]{10, 50, 100, 200, 500, Integer.MAX_VALUE, Double.MAX_VALUE}[power - 1];
    }

}
