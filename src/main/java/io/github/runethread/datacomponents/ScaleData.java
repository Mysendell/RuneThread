package io.github.runethread.datacomponents;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;

public record ScaleData(double scale, Mode mode) {
    public enum Mode {
        POWER(1),
        SCALE(2),
        MODIFIER(3);

        public final int modeVal;

        Mode(int modeVal) {
            this.modeVal = modeVal;
        }
    }

    public void addToTooltip(Item.TooltipContext ctx, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.literal("Scale: " + scale).withStyle(ChatFormatting.YELLOW));
        tooltipAdder.accept(Component.literal("Mode: " + mode).withStyle(ChatFormatting.YELLOW));
    }
}
