package io.github.runethread.customitems.runes;

import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.EntityData;
import io.github.runethread.datacomponents.LocationData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;

public class LocationRuneItem extends Item {
    private final int maxRange;
    private final int defaultStack = 16;

    public LocationRuneItem(Properties properties, int maxRange) {
        super(properties);
        this.maxRange = maxRange;
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if(stack.get(DataComponentRegistry.ENTITY_DATA.get()) != null) {
            stack.remove(DataComponentRegistry.ENTITY_DATA.get());
        }

        if(context.getPlayer().isCrouching())
            return interactLivingEntity(stack, context.getPlayer(), context.getPlayer(), context.getHand());

        BlockPos blockPos = context.getClickedPos();
        int posX = blockPos.getX();
        int posY = blockPos.getY();
        int posZ = blockPos.getZ();

        ItemStack newItemStack = new ItemStack(stack.getItem());
        newItemStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        newItemStack.set(DataComponentRegistry.LOCATION_DATA.get(), new LocationData(posX, posY, posZ));

        stack.shrink(1);
        if (stack.isEmpty()) {
            context.getPlayer().setItemInHand(context.getHand(), newItemStack);
        } else {
            context.getPlayer().getInventory().placeItemBackInInventory(newItemStack);
        }

        return InteractionResult.SUCCESS;
    }

    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if(stack.get(DataComponentRegistry.LOCATION_DATA.get()) != null) {
            stack.remove(DataComponentRegistry.LOCATION_DATA.get());
        }

        ItemStack newItemStack = new ItemStack(stack.getItem());
        newItemStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        int id;
        String name;

        if(player.isCrouching()) {
            id = player.getId();
            name = player.getName().getString();
        }
        else {
            id = interactionTarget.getId();
            name = interactionTarget.getName().getString();
        }


        newItemStack.set(DataComponentRegistry.ENTITY_DATA.get(), new EntityData(id, name));

        stack.shrink(1);
        if (stack.isEmpty()) {
            player.setItemInHand(usedHand, newItemStack);
        } else {
            player.getInventory().placeItemBackInInventory(newItemStack);
        }
        return InteractionResult.SUCCESS;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        LocationData locationData = stack.get(DataComponentRegistry.LOCATION_DATA.get());
        if (locationData != null) {
            locationData.addToTooltip(context, tooltipComponents::add, tooltipFlag);
        }
        EntityData entityData = stack.get(DataComponentRegistry.ENTITY_DATA.get());
        if (entityData != null) {
            entityData.addToTooltip(context, tooltipComponents::add, tooltipFlag);
        }
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        Boolean glint = stack.get(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
        if (glint != null && glint) {
            return 1;
        }
        return defaultStack;
    }

    public int getMaxRange() {
        return maxRange;
    }
}
