package io.github.runethread.datacomponents;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;


// TODO ADD DIMENSIONS
public record LocationData(Integer posX, Integer posY, Integer posZ){

    public LocationData(Integer posX, Integer posY, Integer posZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public void addToTooltip(Item.TooltipContext ctx, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(
                Component.literal("X: " + posX).withStyle(ChatFormatting.RED)
                        .append(Component.literal("  Y: " + posY).withStyle(ChatFormatting.GREEN))
                        .append(Component.literal("  Z: " + posZ).withStyle(ChatFormatting.AQUA)));
    }
}
