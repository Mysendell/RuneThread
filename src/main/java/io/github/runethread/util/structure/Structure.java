package io.github.runethread.util.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;

public class Structure extends HashSet<Structure.StructurePart> {
    private final int rotation;
    /* rotation:
        1 - x axis
        2 - z axis
        4 - y axis
     */

    public Structure(int rotation) {
        super();
        this.rotation = rotation;
    }

    record StructurePart(HolderSet<Block> id, BlockPos pos) {}
}