package io.github.runethread.util;

import io.github.runethread.datacomponents.EntityData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TeleportUtil {
    public enum TeleportResult {
        SUCCESS,
        FAILED_NO_SAFE_LOCATION,
        FAILED_NO_ENTITY
    }

    public static TeleportResult teleportManager(ILocation destination, ILocation reference, ServerLevel level, double maxRadius, double yRadius, ServerPlayer player) {
        LivingEntity toTeleport;
        if (reference == null)
            toTeleport = player;
        else if (reference instanceof EntityData referenceEntityData && referenceEntityData.entity(level) instanceof LivingEntity entity)
            toTeleport = entity;
        else
            return TeleportResult.FAILED_NO_ENTITY;

        return teleportToNearestSafe(toTeleport, level, destination.getLocation(level), maxRadius, yRadius);
    }
    public static TeleportResult teleportManager(BlockPos destination, @NotNull LivingEntity reference, ServerLevel level, double maxRadius, double yRadius) {
        return teleportToNearestSafe(reference, level, destination, maxRadius, yRadius);
    }

    private static TeleportResult teleportToNearestSafe(LivingEntity entity, ServerLevel level, BlockPos center, double maxRadius, double yRadius) {
        List<BlockPos> loadedChunkList = new ArrayList<>();

        for (int dist = 0; dist <= maxRadius; dist++) {
            for (int dy = (int) -yRadius; dy <= yRadius; dy++) {
                boolean stopped = forEachPositionAtDistance(center.getX(), center.getY() + dy, center.getZ(), dist, pos -> {
                    double x = pos.getX() + 0.5;
                    double y = pos.getY();
                    double z = pos.getZ() + 0.5;
                    BlockPos blockPos = new BlockPos((int) x, (int) y, (int) z);
                    if (ChunkUtils.forceLoadChunk(level, pos))
                        loadedChunkList.add(blockPos);
                    if (entity.randomTeleport(x, y, z, true)) {
                        for (BlockPos loadedPos : loadedChunkList)
                            ChunkUtils.removeForceLoadChunk(level, loadedPos);
                        return true;
                    }
                    return false;
                });
                if (stopped) return TeleportResult.SUCCESS;
            }
        }
        for (BlockPos loadedPos : loadedChunkList)
            ChunkUtils.removeForceLoadChunk(level, loadedPos);
        return TeleportResult.FAILED_NO_SAFE_LOCATION;
    }

    private static boolean forEachPositionAtDistance(int x, int y, int z, int dist, Predicate<BlockPos> handler) {
        if (dist == 0) {
            return handler.test(new BlockPos(x, y, z));
        }
        for (int dx = -dist; dx <= dist; dx++) {
            for (int dz = -dist; dz <= dist; dz++) {
                int absdx = Math.abs(dx);
                int absdz = Math.abs(dz);
                int dy = dist - absdx - absdz;
                if (dy >= 0) {
                    if (handler.test(new BlockPos(x + dx, y + dy, z + dz))) return true;
                    if (dy != 0) {
                        if (handler.test(new BlockPos(x + dx, y - dy, z + dz))) return true;
                    }
                }
            }
        }
        return false;
    }
}
