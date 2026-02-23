package io.github.runethread.customitems.runes;

import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.EntityData;
import io.github.runethread.datacomponents.LocationData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponentType;
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
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LocationRuneItem extends Item {
    private final int maxRange;
    private final int defaultStack = 16;

    public LocationRuneItem(Properties properties, int maxRange) {
        super(properties);
        this.maxRange = maxRange;
    }

    @Override
    public @NotNull InteractionResult onItemUseFirst(@NotNull ItemStack stack, UseOnContext context) {
        if (context.getLevel().isClientSide || Objects.requireNonNull(context.getPlayer()).isCrouching())
            return InteractionResult.PASS;

        LocationData locationData = new LocationData(context.getClickedPos(), context.getLevel().dimension());

        return addTargetData(stack, context.getPlayer(), context.getHand(), DataComponentRegistry.LOCATION_DATA.get(), locationData);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity interactionTarget, @NotNull InteractionHand usedHand) {
        if(player instanceof LocalPlayer)
            return InteractionResult.PASS;

        UUID id = interactionTarget.getUUID();
        String name = interactionTarget.getName().getString();
        EntityData entityData = new EntityData(id, name, interactionTarget.level().dimension());

        return addTargetData(stack, player, usedHand, DataComponentRegistry.ENTITY_DATA.get(), entityData);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        if(!player.isCrouching() || level.isClientSide)
            return InteractionResult.PASS;

        UUID id = player.getUUID();
        String name = player.getName().getString();
        EntityData entityData = new EntityData(id, name, player.level().dimension());

        return addTargetData(player.getItemInHand(hand), player, hand, DataComponentRegistry.ENTITY_DATA.get(), entityData);
    }

    private static <T extends Record> InteractionResult addTargetData(ItemStack stack, Player player, InteractionHand hand, DataComponentType<T> dataType, T data) {
        stack = stack.copy();
        ItemStack newItemStack = new ItemStack(stack.getItem());
        newItemStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        newItemStack.set(dataType, data);

        stack.shrink(1);
        if (stack.isEmpty()) {
            player.setItemInHand(hand, newItemStack);
        } else {
            player.setItemInHand(hand, stack);
            player.getInventory().placeItemBackInInventory(newItemStack);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        Component modName = Component.translatable("itemGroup.runethread.runethread_tab").withStyle(ChatFormatting.BLUE);
        LocationData locationData = stack.get(DataComponentRegistry.LOCATION_DATA.get());
        if (locationData != null) {
            locationData.addToTooltip(context, tooltipComponents::add, tooltipFlag);
            tooltipComponents.add(modName);
        }
        EntityData entityData = stack.get(DataComponentRegistry.ENTITY_DATA.get());
        if (entityData != null) {
            entityData.addToTooltip(context, tooltipComponents::add, tooltipFlag);
            tooltipComponents.add(modName);
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
