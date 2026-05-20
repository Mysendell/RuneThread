package io.github.runethread.util.structure;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class StructureLoader extends SimpleJsonResourceReloadListener<StructureCodec> {
    public StructureLoader() {
        super(StructureCodec.CODEC, FileToIdConverter.json("multiblocks"));
    }

    @Override
    protected void apply(Map<ResourceLocation, StructureCodec> data, ResourceManager resourceManager, ProfilerFiller profiler) {
        data.forEach((id, json) -> {
                StructureUtil.addModdedStructure(id, json.parse());
        });
    }

}
