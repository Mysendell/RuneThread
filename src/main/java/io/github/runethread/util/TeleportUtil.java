package io.github.runethread.util;

import io.github.runethread.customblocks.altar.RunicAltarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TeleportUtil {
    public enum TeleportResult {
        SUCCESS,
        FAILED_NO_SAFE_LOCATION,
        FAILED_NO_ENTITY
    }

    public static TeleportResult teleportManager(RunicAltarEntity.DestinationRuneData destination, RunicAltarEntity.DestinationRuneData reference, ServerLevel level, double maxRadius, double yRadius, ServerPlayer player) {
        LivingEntity toTeleport;
        if (reference == null)
            toTeleport = player;
        else if(reference.entity() == null)
            return TeleportResult.FAILED_NO_ENTITY;
        else
            toTeleport = reference.entity();

        return teleportToNearestSafe(toTeleport, level, destination.getBlockPos(), maxRadius, yRadius);
    }
    public static TeleportResult teleportManager(BlockPos destination, @NotNull LivingEntity reference, ServerLevel level, double maxRadius, double yRadius) {
        return teleportToNearestSafe(reference, level, destination, maxRadius, yRadius);
    }

    private static TeleportResult teleportToNearestSafe(LivingEntity entity, ServerLevel level, BlockPos center, double maxRadius, double yRadius) {
        int entityHeight = (int) Math.ceil(entity.getBbHeight());
        List<BlockPos> loadedChunkList = new ArrayList<>();

        // For each distance outwards from the center (spiral/ring)
        for (int dist = 0; dist <= maxRadius; dist++) {
            // For each Y offset within yRadius
            for (int dy = (int) -yRadius; dy <= yRadius; dy++) {
                List<BlockPos> ringPositions = getPositionsAtDistance(center.getX(), center.getY() + dy, center.getZ(), dist);
                for (BlockPos pos : ringPositions) {
                    double x = pos.getX() + 0.5;
                    double y = pos.getY();
                    double z = pos.getZ() + 0.5;
                    BlockPos blockPos = new BlockPos((int) x, (int) y, (int) z);
                    if (ChunkUtils.forceLoadChunk(level, pos))
                        loadedChunkList.add(blockPos);
                    if (entity.randomTeleport(x, y, z, true)) {
                        for (BlockPos loadedPos : loadedChunkList)
                            ChunkUtils.removeForceLoadChunk(level, loadedPos);
                        return TeleportResult.SUCCESS;
                    }
                }
            }
        }
        for (BlockPos loadedPos : loadedChunkList)
            ChunkUtils.removeForceLoadChunk(level, loadedPos);
        return TeleportResult.FAILED_NO_SAFE_LOCATION;
    }

    // Returns all positions at exactly Manhattan distance 'dist' from (x, y, z)
    private static List<BlockPos> getPositionsAtDistance(int x, int y, int z, int dist) {
        List<BlockPos> positions = new ArrayList<>();
        if (dist == 0) {
            positions.add(new BlockPos(x, y, z));
            return positions;
        }
        for (int dx = -dist; dx <= dist; dx++) {
            for (int dz = -dist; dz <= dist; dz++) {
                int absdx = Math.abs(dx);
                int absdz = Math.abs(dz);
                int dy = dist - absdx - absdz;
                if (dy >= 0) {
                    positions.add(new BlockPos(x + dx, y + dy, z + dz));
                    if (dy != 0) positions.add(new BlockPos(x + dx, y - dy, z + dz));
                }
            }
        }
        //Collections.shuffle(positions);
        return positions;
    }
}
