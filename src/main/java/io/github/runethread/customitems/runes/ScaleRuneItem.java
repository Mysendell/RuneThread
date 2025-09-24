package io.github.runethread.customitems.runes;

import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.ScaleData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;


public class ScaleRuneItem extends Item {
    public ScaleRuneItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (player.isCrouching()) {
            ScaleData scaleData = itemStack.getOrDefault(DataComponentRegistry.SCALE_DATA.get(), new ScaleData(0, ScaleData.Mode.POWER));
            int newModeVal = scaleData.mode().modeVal % ScaleData.Mode.values().length;
            ScaleData.Mode newMode = ScaleData.Mode.values()[newModeVal];
            itemStack.set(DataComponentRegistry.SCALE_DATA.get(), new ScaleData(scaleData.scale(), newMode));
        }
        else
            itemStack.set(DataComponentRegistry.SCALE_DATA.get(), new ScaleData(0, ScaleData.Mode.POWER));
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        ScaleData scaleData = stack.get(DataComponentRegistry.SCALE_DATA.get());
        if(scaleData == null)
            return;
        Component modName = Component.translatable("itemGroup.runethread.runethread_tab").withStyle(ChatFormatting.BLUE);
        scaleData.addToTooltip(context, tooltipComponents::add, tooltipFlag);
        tooltipComponents.add(modName);
    } // TODO TOOLTIP NOT WORKING PROPERLY, MOD ID DOESN'T SHOW

    @Override
    public @NotNull Component getName(ItemStack stack) {
        ScaleData scaleData = stack.getOrDefault(DataComponentRegistry.SCALE_DATA.get(), new ScaleData(0, ScaleData.Mode.POWER));
        return Component.literal(super.getName(stack).getString() + ": " + scaleData.mode());
    }

    public static boolean incrementScale(double delta, Player player) {
        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack offHandItem = player.getOffhandItem();
        ItemStack scaleRuneItem;
        double mutliplier;
        double difference;
        InteractionHand hand;
        if(mainHandItem.getItem() instanceof ScaleRuneItem){
            scaleRuneItem = mainHandItem;
            mutliplier = Math.sqrt(10);
            difference = 1;
            hand = InteractionHand.MAIN_HAND;
        }
        else if(offHandItem.getItem() instanceof ScaleRuneItem){
            scaleRuneItem = offHandItem;
            difference = 0.1;
            mutliplier = Math.sqrt(0.1);
            hand = InteractionHand.OFF_HAND;
        }
        else{
            return false;
        }

        int isCrouching = Minecraft.getInstance().options.keyShift.isDown() ? 2 : 0;
        int isSprinting = Minecraft.getInstance().options.keySprint.isDown() ? 3 : -1;

        /*
            !IsCrouching !isSprinting == 0 + 1 - 1 = 0. (10^0.5)^0 = 1
            IsCrouching !isSprinting == 2 + 1 - 1 = 2. (10^0.5)^2 = 10
            !IsCrouching isSprinting == 0 + 1 + 3 = 4. (10^0.5)^4 = 100
            IsCrouching isSprinting == 2 + 1 + 3 = 6. (10^0.5)^6 = 1000
         */

        ScaleData scaleData = scaleRuneItem.getOrDefault(DataComponentRegistry.SCALE_DATA.get(), new ScaleData(1, ScaleData.Mode.POWER));
        double currentScale = scaleData.scale();
        double multiplier = isCrouching == 2 || isSprinting == 3 ? Math.pow(mutliplier, isCrouching + isSprinting + 1) : 1;
        double newScale = currentScale + (delta * multiplier * difference);
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        newScale = Double.parseDouble(decimalFormat.format(newScale));
        scaleRuneItem.set(DataComponentRegistry.SCALE_DATA.get(), new ScaleData(newScale, scaleData.mode()));
        player.swing(hand);
        return true;
    }
}
