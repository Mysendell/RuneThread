package io.github.runethread.util;

import io.github.runethread.customblocks.StructureCenterEntity;
import io.github.runethread.customblocks.StructurePartBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.HashSet;
import java.util.Set;

public class StructureCheckerUtil {
    public record StructurePart(BlockPos corner1, BlockPos corner2, DeferredBlock<? extends Block> block) {
    }

    public static Set<BlockPos> ValidateAndAddStructure(StructurePart[] structure, BlockPos origin, Level level, int structureSize, StructureCenterEntity entity) {
        Set<BlockPos> structureBlocks = new HashSet<>(structureSize);
        for (StructurePart part : structure) {
            ValidateAndAddArea(part.corner1, part.corner2, origin, level, (DeferredBlock<StructurePartBlock>) part.block, structureBlocks, entity);
        }
        return structureBlocks;
    }

    public static Set<BlockPos> ValidateAndAddArea(BlockPos corner1, BlockPos corner2, BlockPos origin, Level level, DeferredBlock<StructurePartBlock> block, Set<BlockPos> structureBlocks, StructureCenterEntity entity) {
        int minX = Math.min(corner1.getX(), corner2.getX());
        int maxX = Math.max(corner1.getX(), corner2.getX());
        int minY = Math.min(corner1.getY(), corner2.getY());
        int maxY = Math.max(corner1.getY(), corner2.getY());
        int minZ = Math.min(corner1.getZ(), corner2.getZ());
        int maxZ = Math.max(corner1.getZ(), corner2.getZ());

        for (int x = minX; x <= maxX; x++)
            for (int y = minY; y <= maxY; y++)
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos checkPos = origin.offset(x, y, z);
                    if (level.getBlockState(checkPos).is(block)) {
                        /*if (level.getBlockState(checkPos).getBlock() instanceof IStructurePart structurePart)
                            structurePart.setStructureCenter(entity);*/
                        structureBlocks.add(checkPos);
                    }
                }


        return structureBlocks;
    }

    public static boolean isValidStructure(StructurePart[] structure, BlockPos origin, Level level) {
        for (StructurePart part : structure) {
            if (!isAreaValid(part.corner1, part.corner2, origin, level, (DeferredBlock<Block>) part.block))
                return false;
        }
        return true;
    }

    public static boolean isAreaValid(BlockPos corner1, BlockPos corner2, BlockPos origin, Level level, DeferredBlock<Block> block) {
        int minX = Math.min(corner1.getX(), corner2.getX());
        int maxX = Math.max(corner1.getX(), corner2.getX());
        int minY = Math.min(corner1.getY(), corner2.getY());
        int maxY = Math.max(corner1.getY(), corner2.getY());
        int minZ = Math.min(corner1.getZ(), corner2.getZ());
        int maxZ = Math.max(corner1.getZ(), corner2.getZ());

        for (int x = minX; x <= maxX; x++)
            for (int y = minY; y <= maxY; y++)
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos checkPos = origin.offset(x, y, z);
                    if (!level.getBlockState(checkPos).is(block))
                        return false;
                }
        return true;
    }
}
