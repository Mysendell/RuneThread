package io.github.runethread.customitems.runes;

import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.PowerData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PowerRuneItem extends Item {
    public PowerRuneItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        PowerData powerData = stack.get(DataComponentRegistry.POWER_DATA.get());
        if (powerData == null)
            return;
        powerData.addToTooltip(context, tooltipComponents::add, tooltipFlag);
    }
}
