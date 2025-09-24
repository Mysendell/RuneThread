package io.github.runethread.datacomponents;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;


// TODO ADD DIMENSIONS
public record EntityData(int UUID, String name) {

    public EntityData(int UUID, String name) {
        this.UUID = UUID;
        this.name = name;
    }

    public void addToTooltip(Item.TooltipContext ctx, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.literal(name + "#" + UUID).withStyle(ChatFormatting.YELLOW));
    }

    public BlockPos getLocation(Level level) {
        if (level.getEntity(UUID) != null) {
            return level.getEntity(UUID).blockPosition();
        } else {
            return null;
        }
    }
}
