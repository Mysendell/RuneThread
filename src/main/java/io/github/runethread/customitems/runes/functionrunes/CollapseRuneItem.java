package io.github.runethread.customitems.runes.functionrunes;

import io.github.runethread.RuneThread;
import io.github.runethread.customblocks.altar.RunicAltarEntity;
import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.datacomponents.DataComponentRegistry;
import io.github.runethread.datacomponents.StructureData;
import io.github.runethread.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CollapseRuneItem extends MainRuneItem {
    private String nameBase;
    private boolean writeToDisk = true;

    public CollapseRuneItem(Properties properties, int cost, int scale, double scalingCost, double breakChance) {
        super(properties, cost, scale, scalingCost, breakChance);
    }

    /*
        TODO check colors
        TODO Add reverse to remove air blocks
        TODO Add direction arrow support (center not being actual center)
        TODO add config for writeToDisk and ignoreEntities(living rune)
        TODO allow setting nameBase
        TODO support for multiple scale runes (define 3 dimensions)
    */

    @Override
    public @NotNull InteractionResult onItemUseFirst(@NotNull ItemStack stack, @NotNull UseOnContext context) {
//        Level level = context.getLevel();
//        if (level.isClientSide())
//            return InteractionResult.PASS;
//        Player player = context.getPlayer();
//        if (player == null)
//            return InteractionResult.FAIL;
//        if (!player.isCrouching())
//            return InteractionResult.PASS;
//
//        ServerLevel serverLevel =  (ServerLevel) level;
//        BlockPos center = context.getClickedPos();
//        ResourceLocation structureName;
//        try {
//            structureName = Objects.requireNonNull(stack.get(DataComponentRegistry.STRUCTURE_NAME.get())).structureName();
//        } catch (NullPointerException e) {
//            return InteractionResult.PASS;
//        }
//
//        StructureTemplate structureTemplate = StructureUtil.loadStructure(structureName, serverLevel);
//        Vec3i size = structureTemplate.getSize();
//
//        StructureUtil.placeStructureViaTag(listTag, center, level);
//        stack.shrink(1);
//        player.setItemInHand(context.getHand(), stack);
//        TeleportUtil.teleportManager(new LocationData(center, level.dimension()), player, (ServerLevel) level, radius, radius);
        return InteractionResult.SUCCESS;
    }

    @Override
    public RunicAltarEntity.RitualState perform(ServerLevel level, ServerPlayer player, ItemStack mainStack, double finalScale, ILocation destination, @Nullable ILocation reference, BlockPos origin, ModifierMap additionalData) {
        String playerName = player.getName().getString();
        ResourceLocation structureName = ResourceLocation.fromNamespaceAndPath(RuneThread.MODID, playerName + "#" + nameBase);
        BlockPos center = destination.getLocation(level);
        boolean ignoreEntities = true;
        Vec3i size = new Vec3i((int) finalScale, (int) finalScale, (int) finalScale); //PLACEHOLDER

        if(!StructureUtil.saveStructure(center, size, ignoreEntities, writeToDisk, level, structureName, playerName)) {
            ChatUtils.sendErrorMessagePlayer(playerName, "ERROR CREATING STRUCTURE! Report this as a bug", level);
            return RunicAltarEntity.RitualState.ALMOST;
        }

        ItemStack finalStack = mainStack.copy();
        finalStack.setCount(1);
        mainStack.shrink(1);
        finalStack.set(DataComponentRegistry.STRUCTURE_NAME, new StructureData(structureName));
        finalStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        InventoryUtil.dropStack(origin, level, finalStack);

        return RunicAltarEntity.RitualState.SUCCESS;
    }
}
