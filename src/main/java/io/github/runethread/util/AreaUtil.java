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

    private static void fourPointSymmetry(BlockPos center, int x, int z, AreaInterface action, ServerLevel level, List<BlockPos> loadedChunkList) {
        int symX = (2 * center.getX()) - x;
        int symZ = (2 * center.getZ()) - z;
        BlockPos pos1 = new BlockPos(x, center.getY(), z);
        BlockPos pos2 = new BlockPos(x, center.getY(), symZ);
        BlockPos pos3 = new BlockPos(symX, center.getY(), z);
        BlockPos pos4 = new BlockPos(symX, center.getY(), symZ);
        if (ChunkUtils.forceLoadChunk(level, pos1)) loadedChunkList.add(pos1);
        action.action(pos1);
        if (ChunkUtils.forceLoadChunk(level, pos2)) loadedChunkList.add(pos2);
        action.action(pos2);
        if (ChunkUtils.forceLoadChunk(level, pos3)) loadedChunkList.add(pos3);
        action.action(pos3);
        if (ChunkUtils.forceLoadChunk(level, pos4)) loadedChunkList.add(pos4);
        action.action(pos4);
    }

    private static void fourPointSymmetry(BlockPos center, int x, int z, int y, AreaInterface action, ServerLevel level, List<BlockPos> loadedChunkList) {
        int symX = (2 * center.getX()) - x;
        int symZ = (2 * center.getZ()) - z;
        BlockPos pos1 = new BlockPos(x, y, z);
        BlockPos pos2 = new BlockPos(x, y, symZ);
        BlockPos pos3 = new BlockPos(symX, y, z);
        BlockPos pos4 = new BlockPos(symX, y, symZ);
        if (ChunkUtils.forceLoadChunk(level, pos1)) loadedChunkList.add(pos1);
        action.action(pos1);
        if (ChunkUtils.forceLoadChunk(level, pos2)) loadedChunkList.add(pos2);
        action.action(pos2);
        if (ChunkUtils.forceLoadChunk(level, pos3)) loadedChunkList.add(pos3);
        action.action(pos3);
        if (ChunkUtils.forceLoadChunk(level, pos4)) loadedChunkList.add(pos4);
        action.action(pos4);
    }


    private static void eightPointSymmetry(BlockPos center, int x, int z, int y, AreaInterface action, ServerLevel level, List<BlockPos> loadedChunkList) {
        int symY = (2 * center.getY()) - y;
        fourPointSymmetry(center, x, z, y, action, level, loadedChunkList);
        fourPointSymmetry(center, x, z, symY, action, level, loadedChunkList);
    }

    public static void IterateAreaSphere(ServerLevel level, BlockPos center, double radius, AreaInterface action) {
        Bounding3D bounding = new Bounding3D(center, radius, level.getMinY(), level.getMaxY());
        double radiusSq = radius * radius;

        List<BlockPos> loadedChunkList = new ArrayList<>();

        for (int x = center.getX(); x <= bounding.maxX(); x++) {
            for (int y = center.getY(); y <= bounding.maxY(); y++) {
                for (int z = center.getZ(); z <= bounding.maxZ(); z++) {
                    double dx = x + 0.5 - center.getX();
                    double dy = y + 0.5 - center.getY();
                    double dz = z + 0.5 - center.getZ();
                    if (dx * dx + dy * dy + dz * dz <= radiusSq) {
                        eightPointSymmetry(center, x, z, y, action, level, loadedChunkList);
                    }
                }
            }
        }

        for (BlockPos pos : loadedChunkList) {
            ChunkUtils.removeForceLoadChunk(level, pos);
        }
    }

    public static void IteratePerimeterCircle(ServerLevel level, BlockPos center, double radius, AreaInterface action) {
        Bounding2D bounding = new Bounding2D(center, radius);
        double radiusSq = radius * radius;
        double innerRadiusSq = (radius - 1) * (radius - 1);

        List<BlockPos> loadedChunkList = new ArrayList<>();

        for (int x = bounding.minX(); x <= center.getX(); x++) {
            for (int z = bounding.minZ(); z <= center.getZ(); z++) {
                double dx = x + 0.5 - center.getX();
                double dz = z + 0.5 - center.getZ();

                double distSq = dx * dx + dz * dz;
                if (distSq <= radiusSq && distSq >= innerRadiusSq)
                    fourPointSymmetry(center, x, z, action, level, loadedChunkList);

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

        for (int x = bounding.minX(); x <= center.getX(); x++) {
            for (int z = bounding.minZ(); z <= center.getZ(); z++) {
                double dx = x + 0.5 - center.getX();
                double dz = z + 0.5 - center.getZ();
                if (dx * dx + dz * dz <= radiusSq)
                    fourPointSymmetry(center, x, z, action, level, loadedChunkList);
            }
        }

        for (BlockPos pos : loadedChunkList) {
            ChunkUtils.removeForceLoadChunk(level, pos);
        }
    }

    public static void IterateAreaBox(ServerLevel level, BlockPos center, double radius, AreaInterface action) {
        Bounding3D bounding = new Bounding3D(center, radius, level.getMinY(), level.getMaxY());

        List<BlockPos> loadedChunkList = new ArrayList<>();

        for (int x = center.getX(); x < bounding.maxX(); x++) {
            for (int y = center.getY(); y < bounding.maxY(); y++) {
                for (int z = center.getZ(); z < bounding.maxZ(); z++) {
                    eightPointSymmetry(center, x, z, y, action, level, loadedChunkList);
                }
            }
        }

        for (BlockPos pos : loadedChunkList) {
            ChunkUtils.removeForceLoadChunk(level, pos);
        }
    }
}
