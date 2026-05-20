package io.github.runethread.util.structure;

import io.github.runethread.customblocks.structure.StructureCenterBlock;
import io.github.runethread.customblocks.structure.StructureCenterEntity;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.HashMap;
import java.util.Map;

import static io.github.runethread.RuneThread.MODID;

public class StructureUtil {
    private static Map<ResourceLocation, Structure> StructureMap = new HashMap<>();
    public static void addModdedStructure(ResourceLocation resourceLocation, Structure structure) {
        StructureMap.put(resourceLocation, structure);
    }
    public static Structure getModdedStructure(String structureName) throws ResourceLocationException {
        Structure structure = StructureMap.get(ResourceLocation.fromNamespaceAndPath(MODID, structureName));
        if (structure == null)
            throw new ResourceLocationException("Unknown structure: " + structureName);
        return structure;
    }

    public static boolean ValidateStructure(String structureName, StructureCenterEntity center, ServerLevel level) throws ResourceLocationException {
        BlockPos origin = center.getBlockPos();
        Structure structure = getModdedStructure(structureName);

        for(Structure.StructurePart structurePart : structure){
            BlockPos pos = structurePart.pos().offset(origin);
            BlockState state = level.getBlockState(pos);
            ResourceKey<Block> key = BuiltInRegistries.BLOCK.getResourceKey(state.getBlock()).orElseThrow();

            if(structurePart.id().contains(level.registryAccess().holderOrThrow(key))) {
                if(state.hasProperty(StructureCenterBlock.STRUCTURED)){
                    level.setBlock(pos, state.setValue(StructureCenterBlock.STRUCTURED, true), 2);
                    }
            }
            else{
                return false;
            }
        }
        return true;
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
