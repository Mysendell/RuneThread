package io.github.runethread.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeleportUtil {
    /**
     * Searches for the nearest safe teleport position in a 3D Manhattan spiral outwards.
     * @param entity The entity to teleport
     * @param level The world
     * @param center The center position to search around
     * @param maxRadius How far to search (in blocks)
     * @param yRadius How far up/down to check
     * @return true if teleported, false otherwise
     */
    public static boolean teleportToNearestSafe(LivingEntity entity, ServerLevel level, BlockPos center, int maxRadius, int yRadius) {
        int entityHeight = (int)Math.ceil(entity.getBbHeight());
        List<BlockPos> loadedChunkList = new ArrayList<>();

        // For each distance outwards from the center (spiral/ring)
        for (int dist = 0; dist <= maxRadius; dist++) {
            // For each Y offset within yRadius
            for (int dy = -yRadius; dy <= yRadius; dy++) {
                List<BlockPos> ringPositions = getPositionsAtDistance(center.getX(), center.getY() + dy, center.getZ(), dist);
                for (BlockPos pos : ringPositions) {
                    double x = pos.getX() + 0.5;
                    double y = pos.getY();
                    double z = pos.getZ() + 0.5;
                    BlockPos blockPos = new BlockPos((int) x, (int) y, (int) z);
                    if(ChunkUtils.forceLoadChunk(level, pos))
                        loadedChunkList.add(blockPos);
                    if (entity.randomTeleport(x, y, z, true)) {
                        for(BlockPos loadedPos : loadedChunkList) {
                            ChunkUtils.removeForceLoadChunk(level, loadedPos);
                        }
                        return true;
                    }
                }
            }
        }
        for(BlockPos loadedPos : loadedChunkList) {
            ChunkUtils.removeForceLoadChunk(level, loadedPos);
        }
        return false;
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
        // Optionally, shuffle to avoid always favoring certain axes
        Collections.shuffle(positions);
        return positions;
    }
}
