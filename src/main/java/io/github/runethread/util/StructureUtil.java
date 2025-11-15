package io.github.runethread.util;

import io.github.runethread.customblocks.structure.StructureCenterEntity;
import io.github.runethread.customblocks.structure.StructurePartBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.HashSet;
import java.util.Set;

public class StructureUtil {
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

    public static CompoundTag serializeBlock(BlockPos pos, BlockPos center, Level level) {
        CompoundTag tag = new CompoundTag();
        BlockPos newPos = pos.offset(center.multiply(-1));
        tag.putInt("x", newPos.getX());
        tag.putInt("y", newPos.getY());
        tag.putInt("z", newPos.getZ());
        BlockState blockState = level.getBlockState(pos);
        tag.put("blockState", NbtUtils.writeBlockState(blockState));
        if(level.getBlockEntity(pos) != null)
            tag.put("blockEntity", level.getBlockEntity(pos).saveWithFullMetadata(level.registryAccess()));
        return tag;
    }

    public static void placeBlockViaTag(CompoundTag tag, BlockPos center, Level level) {
        int x = tag.getInt("x");
        int y = tag.getInt("y");
        int z = tag.getInt("z");
        BlockPos pos = new BlockPos(x, y, z).offset(center);
        BlockState blockState = NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK), tag.getCompound("blockState"));
        level.setBlock(pos, blockState, 3);
        if(tag.contains("blockEntity") && level.getBlockEntity(pos) != null)
            level.getBlockEntity(pos).loadCustomOnly(tag.getCompound("blockEntity"), level.registryAccess());
    }

    public static void placeStructureViaTag(ListTag listTag, BlockPos center, Level level) {
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag tag = listTag.getCompound(i);
            placeBlockViaTag(tag, center, level);
        }
    }
}
