package io.github.runethread.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;

public class AreaUtil {
    public interface AreaInterface {
        void action(Object... args);
    }

    private record Bounding3D(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        Bounding3D(BlockPos center, double radius, int levelMinY, int levelMaxY) {
            this(center.getX() - (int) Math.round(radius),
                    center.getX() + (int) Math.round(radius),
                    Math.max(levelMinY, center.getY() - (int) Math.round(radius)),
                    Math.min(levelMaxY - 1, center.getY() + (int) Math.round(radius)),
                    center.getZ() - (int) Math.round(radius),
                    center.getZ() + (int) Math.round(radius));
        }
    }

    private record Bounding2D(int minX, int maxX, int minZ, int maxZ) {
        Bounding2D(BlockPos center, double radius) {
            this(center.getX() - (int) Math.round(radius),
                    center.getX() + (int) Math.round(radius),
                    center.getZ() - (int) Math.round(radius),
                    center.getZ() + (int) Math.round(radius));
        }
    }

    public static void IterateAreaSphere(ServerLevel level, BlockPos center, double radius, AreaInterface action) {
        Bounding3D bounding = new Bounding3D(center, radius, level.getMinY(), level.getMaxY());
        double radiusSq = radius * radius;

        List<BlockPos> loadedChunkList = new ArrayList<>();

        for (int x = bounding.minX(); x <= bounding.maxX(); x++) {
            for (int y = bounding.minY(); y <= bounding.maxY(); y++) {
                for (int z = bounding.minZ(); z <= bounding.maxZ(); z++) {
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
        Bounding2D bounding = new Bounding2D(center, radius);
        double radiusSq = radius * radius;

        List<BlockPos> loadedChunkList = new ArrayList<>();

        for (int x = bounding.minX(); x <= bounding.maxX(); x++) {
            for (int z = bounding.minZ(); z <= bounding.maxZ(); z++) {
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
        Bounding3D bounding = new Bounding3D(center, radius, level.getMinY(), level.getMaxY());

        List<BlockPos> loadedChunkList = new ArrayList<>();

        for (int x = bounding.minX(); x < bounding.maxX(); x++) {
            for (int y = bounding.minY(); y < bounding.maxY(); y++) {
                for (int z = bounding.minZ(); z < bounding.maxZ(); z++) {
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
