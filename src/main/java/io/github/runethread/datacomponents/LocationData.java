package io.github.runethread.datacomponents;

import io.github.runethread.util.ILocation;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;


// TODO ADD DIMENSIONS
public record LocationData(Integer posX, Integer posY, Integer posZ) implements ILocation {

    public LocationData(BlockPos pos){
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void addToTooltip(Item.TooltipContext ctx, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(
                Component.literal("X: " + posX).withStyle(ChatFormatting.RED)
                        .append(Component.literal("  Y: " + posY).withStyle(ChatFormatting.GREEN))
                        .append(Component.literal("  Z: " + posZ).withStyle(ChatFormatting.AQUA)));
    }

    @Override
    public BlockPos getLocation(Level level) {
        return new BlockPos(posX, posY, posZ);
    }
}
