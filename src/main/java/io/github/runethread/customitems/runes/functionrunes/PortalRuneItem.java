package io.github.runethread.customitems.runes.functionrunes;

import io.github.runethread.customblocks.altar.RunicAltarEntity;
import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.util.ChatUtils;
import io.github.runethread.util.ILocation;
import io.github.runethread.util.TeleportUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PortalRuneItem extends MainRuneItem{
    public PortalRuneItem(Properties properties, int cost, double scale, double scalingCost, double breakChance) {
        super(properties, cost, scale, scalingCost, breakChance);
    }

    @Override
    public RunicAltarEntity.RitualState perform(ServerLevel level, ServerPlayer player, ItemStack mainStack, double finalScale, ILocation destination, @Nullable ILocation reference, BlockPos origin, Map<String, Object> additionalData) {
        TeleportUtil.TeleportResult result = TeleportUtil.teleportManager(destination, reference, level, 10, 10, player);
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
        };
    }

    @Override
    public boolean insideRange(double scale, double range, double distance) {
        return distance <= range * scale;
    }
}
