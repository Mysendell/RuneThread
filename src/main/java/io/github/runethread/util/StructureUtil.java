package io.github.runethread.util;

import io.github.runethread.customblocks.structure.StructurePartBlock;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;

public class StructureUtil {
    public record StructurePart(BlockPos corner1, BlockPos corner2, DeferredBlock<? extends Block> block) {
    }

    public static Structure ValidateAndAddStructure(StructurePart[] structure, BlockPos origin, Level level, int structureSize) {
        Structure structureBlocks = new Structure(structureSize);
        for (StructurePart part : structure) {
            ValidateAndAddArea(part.corner1, part.corner2, origin, level, (DeferredBlock<StructurePartBlock>) part.block, structureBlocks);
        }
        return structureBlocks;
    }

    public static void ValidateAndAddArea(BlockPos corner1, BlockPos corner2, BlockPos origin, Level level, DeferredBlock<StructurePartBlock> block, Structure structureBlocks) {
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
                    if (level.getBlockState(checkPos).is(block))
                        structureBlocks.add(checkPos);
                    else
                        structureBlocks.setValid(false);
                }
    }

    public static boolean saveStructure(BlockPos center, Vec3i size, boolean ignoreEntities, boolean writeToDisk, ServerLevel level, ResourceLocation structureName, String author) {
        StructureTemplateManager structuretemplatemanager = level.getStructureManager();
        StructureTemplate structuretemplate;

        try {
            structuretemplate = structuretemplatemanager.getOrCreate(structureName);
        } catch (ResourceLocationException e) {
            e.printStackTrace();
            return false;
        }

        structuretemplate.fillFromWorld(level, center, size, !ignoreEntities, Blocks.STRUCTURE_VOID);
        structuretemplate.setAuthor(author);

        if (writeToDisk) {
            try {
                return structuretemplatemanager.save(structureName);
            } catch (ResourceLocationException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }

    public static StructureTemplate loadStructure(@NotNull ResourceLocation structureName, ServerLevel level){
        return level.getStructureManager().get(structureName).orElse(null);
    }

    public static boolean placeStructure(StructureTemplate structureTemplate, ServerLevel level){
//        StructurePlaceSettings structureplacesettings = new StructurePlaceSettings()
//                .setMirror(mirror)
//                .setRotation(rotation)
//                .setIgnoreEntities(ignoreEntities);
//        BlockPos blockpos = getBlockPos().offset(structurePos);
//        structureTemplate.placeInWorld(level, blockpos, blockpos, structureplacesettings, RandomSource.create(Util.getMillis()), 2);
        return true;
    }
}
