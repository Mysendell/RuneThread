package io.github.runethread.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;

public class AreaUtil {
    public interface AreaInterface {
        void action(Object... args);
    }

    public static void IterateAreaSphere(ServerLevel level, BlockPos center, double radius, AreaInterface action) {
        int minX = center.getX() - (int) Math.round(radius);
        int maxX = center.getX() + (int) Math.round(radius);
        int minY = Math.max(level.getMinY(), center.getY() - (int) Math.round(radius));
        int maxY = Math.min(level.getMaxY() - 1, center.getY() + (int) Math.round(radius));
        int minZ = center.getZ() - (int) Math.round(radius);
        int maxZ = center.getZ() + (int) Math.round(radius);
        double radiusSq = radius * radius;

        List<BlockPos> loadedChunkList = new ArrayList<>();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    double dx = x + 0.5 - center.getX();
                    double dy = y + 0.5 - center.getY();
                    double dz = z + 0.5 - center.getZ();
                    if (dx * dx + dy * dy + dz * dz <= radiusSq) {
                        BlockPos pos = new BlockPos(x, y, z);
                        if (ChunkUtils.forceLoadChunk(level, pos))
                            loadedChunkList.add(pos);
                        action.action(pos);
                    }
                }
            }
        }

        for (BlockPos pos : loadedChunkList) {
            ChunkUtils.removeForceLoadChunk(level, pos);
        }
    }

    public static void IterateAreaCircle(ServerLevel level, BlockPos center, double radius, AreaInterface action) {
        int minX = center.getX() - (int) Math.ceil(radius);
        int maxX = center.getX() + (int) Math.ceil(radius);
        int minZ = center.getZ() - (int) Math.ceil(radius);
        int maxZ = center.getZ() + (int) Math.ceil(radius);
        double radiusSq = radius * radius;

        List<BlockPos> loadedChunkList = new ArrayList<>();

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                double dx = x + 0.5 - center.getX();
                double dz = z + 0.5 - center.getZ();
                if (dx * dx + dz * dz <= radiusSq) {
                    BlockPos pos = new BlockPos(x, center.getY(), z);
                    if (ChunkUtils.forceLoadChunk(level, pos))
                        loadedChunkList.add(pos);
                    action.action(pos);
                }
            }
        }

        for (BlockPos pos : loadedChunkList) {
            ChunkUtils.removeForceLoadChunk(level, pos);
        }
    }

    public static void IterateAreaBox(ServerLevel level, BlockPos center, double radius, AreaInterface action) {
        int minX = center.getX() - (int) Math.round(radius);
        int maxX = center.getX() + (int) Math.round(radius);
        int minY = Math.max(level.getMinY(), center.getY() - (int) Math.round(radius));
        int maxY = Math.min(level.getMaxY() - 1, center.getY() + (int) Math.round(radius));
        int minZ = center.getZ() - (int) Math.round(radius);
        int maxZ = center.getZ() + (int) Math.round(radius);

        List<BlockPos> loadedChunkList = new ArrayList<>();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (ChunkUtils.forceLoadChunk(level, pos))
                        loadedChunkList.add(pos);
                    action.action(pos);
                }
            }
        }

        for (BlockPos pos : loadedChunkList) {
            ChunkUtils.removeForceLoadChunk(level, pos);
        }
    }
}
