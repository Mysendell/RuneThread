package io.github.runethread.customitems.runes;

import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.PowerData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class PowerRuneItem extends Item {
    public PowerRuneItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        PowerData powerData = stack.get(DataComponentRegistry.POWER_DATA.get());
        if (powerData == null)
            return;
        powerData.addToTooltip(context, tooltipComponents::add, tooltipFlag);
        Component modName = Component.translatable("itemGroup.runethread.runethread_tab").withStyle(ChatFormatting.BLUE);
        tooltipComponents.add(modName);
    }
}
