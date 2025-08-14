package io.github.runethread.customitems.runes;

import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.EntityData;
import io.github.runethread.datacomponents.LocationData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
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

public class LocationRuneItem extends Item {
    private final int maxRange;
    private final int defaultStack = 16;

    public LocationRuneItem(Properties properties, int maxRange) {
        super(properties);
        this.maxRange = maxRange;
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (context.getPlayer().isCrouching())
            return InteractionResult.PASS;

        BlockPosSpreader blockPosSpreader = spreadBlockPos(context.getClickedPos());
        LocationData locationData = new LocationData(blockPosSpreader.posX(), blockPosSpreader.posY(), blockPosSpreader.posZ());

        return addTargetData(stack, context.getPlayer(), context.getHand(), DataComponentRegistry.LOCATION_DATA.get(), locationData);
    }

    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        int id = interactionTarget.getId();
        String name = interactionTarget.getName().getString();
        EntityData entityData = new EntityData(id, name);

        return addTargetData(stack, player, usedHand, DataComponentRegistry.ENTITY_DATA.get(), entityData);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if(!player.isCrouching())
            return InteractionResult.PASS;

        int id = player.getId();
        String name = player.getName().getString();
        EntityData entityData = new EntityData(id, name);

        return addTargetData(player.getItemInHand(hand), player, hand, DataComponentRegistry.ENTITY_DATA.get(), entityData);
    }

    private static @NotNull BlockPosSpreader spreadBlockPos(BlockPos blockPos) {
        int posX = blockPos.getX();
        int posY = blockPos.getY();
        int posZ = blockPos.getZ();
        BlockPosSpreader blockPosSpreader = new BlockPosSpreader(posX, posY, posZ);
        return blockPosSpreader;
    }

    private record BlockPosSpreader(int posX, int posY, int posZ) {
    }

    private static <T extends Record> InteractionResult addTargetData(ItemStack stack, Player player, InteractionHand hand, DataComponentType<T> dataType, T data) {
        if(player instanceof LocalPlayer)
            return InteractionResult.PASS;

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
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
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
