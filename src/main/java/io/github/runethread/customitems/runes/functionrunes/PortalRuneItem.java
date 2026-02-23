package io.github.runethread.customitems.runes.functionrunes;

import io.github.runethread.customblocks.altar.RunicAltarEntity;
import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.datacomponents.EntityData;
import io.github.runethread.util.ChatUtils;
import io.github.runethread.util.ILocation;
import io.github.runethread.util.ModifierMap;
import io.github.runethread.util.TeleportUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class PortalRuneItem extends MainRuneItem{
    public PortalRuneItem(Properties properties, int cost, double scale, double scalingCost, double breakChance) {
        super(properties, cost, scale, scalingCost, breakChance);
    }

    @Override
    public RunicAltarEntity.RitualState perform(ServerLevel level, ServerPlayer player, ItemStack mainStack, double finalScale, ILocation destination, @Nullable ILocation reference, BlockPos origin, ModifierMap additionalData) {
        if(reference == null)
            reference = new EntityData(player.getUUID(), player.getName().getString(), player.level().dimension());
        ServerLevel destinationLevel = level.getServer().getLevel(destination.getDimension(level));
        ServerLevel referenceLevel = level.getServer().getLevel(reference.getDimension(level));
        if(destinationLevel != referenceLevel) {
            ChatUtils.sendErrorMessagePlayer(player.getName().getString(), "Teleport failed: interdimensional teleport without proper runes", level);
            return RunicAltarEntity.RitualState.ALMOST;
        }

        TeleportUtil.TeleportResult result = TeleportUtil.teleportManager(destination, reference, level, 10, 10);
        return switch (result) {
            case FAILED_NO_SAFE_LOCATION -> {
                ChatUtils.sendErrorMessagePlayer(player.getName().getString(), "Teleport failed: No safe location found!", level);
                yield RunicAltarEntity.RitualState.ALMOST;
            }
            case FAILED_NO_ENTITY -> {
                ChatUtils.sendErrorMessagePlayer(player.getName().getString(), "Teleport failed: No entity is provided in the reference rune", level);
                yield RunicAltarEntity.RitualState.ALMOST;
            }
            case SUCCESS -> RunicAltarEntity.RitualState.SUCCESS;
            case FAILED_NO_LEVEL -> {
                ChatUtils.sendErrorMessagePlayer(player.getName().getString(), "Teleport failed: Could not find destination's dimension", level);
                yield RunicAltarEntity.RitualState.ALMOST;
            }
        };
    }

    @Override
    public boolean insideRange(double scale, double range, double distance) {
        return distance <= range * scale;
    }
}
