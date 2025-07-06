package io.github.runethread.customblocks;

import net.minecraft.world.level.block.state.properties.BooleanProperty;

public interface IStructurePart {
    BooleanProperty STRUCTURED = BooleanProperty.create("structured");
    StructureCenterEntity getStructureCenter();
    void setStructureCenter(StructureCenterEntity structureCenter);
}
