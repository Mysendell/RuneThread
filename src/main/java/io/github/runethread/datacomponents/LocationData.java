package io.github.runethread.datacomponents;

import io.github.runethread.util.ILocation;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public record LocationData(Integer posX, Integer posY, Integer posZ, ResourceKey<Level> dimension) implements ILocation {

    public LocationData(BlockPos pos, ResourceKey<Level> dimension){
        this(pos.getX(), pos.getY(), pos.getZ(), dimension);
    }

    @Override
    public void addToTooltip(Item.TooltipContext ctx, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(
                Component.literal("X: " + posX).withStyle(ChatFormatting.RED)
                        .append(Component.literal("  Y: " + posY).withStyle(ChatFormatting.GREEN))
                        .append(Component.literal("  Z: " + posZ).withStyle(ChatFormatting.AQUA))
                        .append(Component.literal(" Dimension: " + dimension.location().getPath()).withStyle(ChatFormatting.GREEN)));
    }

    @Override
    public ResourceKey<Level> getDimension(ServerLevel level) {
        return dimension;
    }

    @Override
    public BlockPos getLocation(ServerLevel level) {
        return new BlockPos(posX, posY, posZ);
    }

    public ResourceLocation dimensionLocation() {
        return dimension.location();
    }

    public LocationData(Integer posX, Integer posY, Integer posZ, ResourceLocation dimensionLocation) {
        this(
                posX,
                posY,
                posZ,
                ResourceKey.create(Registries.DIMENSION, dimensionLocation)
        );
    }
}
