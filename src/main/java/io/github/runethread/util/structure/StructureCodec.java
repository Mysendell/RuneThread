package io.github.runethread.util.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public record StructureCodec(Map<String, HolderSet<Block>> mapping, int rotation, BlockPos center,
                             List<List<String>> structure) {

    public static final Codec<StructureCodec> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.unboundedMap(Codec.STRING, RegistryCodecs.homogeneousList(Registries.BLOCK))
                            .fieldOf("mapping")
                            .forGetter(StructureCodec::mapping),
                    Codec.INT.fieldOf("rotation").forGetter(StructureCodec::rotation),
                    BlockPos.CODEC.fieldOf("center").forGetter(StructureCodec::center),
                    Codec.list(Codec.list(Codec.STRING)).fieldOf("structure").forGetter(StructureCodec::structure)
            ).apply(instance, StructureCodec::new)
    );

    public Structure parse() {
        Structure structureObj = new Structure(rotation);

        Iterator<List<String>> it1 = structure.iterator();
        for (int y = structure.size() - 1; it1.hasNext(); y--) {
            List<String> structure2 = it1.next();
            Iterator<String> it2 = structure2.iterator();

            for (int x = structure2.size() - 1; it2.hasNext(); x--) {
                char[] structure3 = it2.next().toCharArray();

                for (int z = structure3.length - 1; z >= 0; z--) {
                    if (structure3[z] == ' ')
                        continue;
                    BlockPos blockPos = new BlockPos(x, y, z).subtract(center);
                    HolderSet<Block> holderSet = mapping.get(Character.toString(structure3[z]));
                    structureObj.add(new Structure.StructurePart(holderSet, blockPos));
                }
            }
        }
        return structureObj;
    }
}
