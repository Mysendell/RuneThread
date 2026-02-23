package io.github.runethread.util;

import net.minecraft.core.BlockPos;

import java.util.HashSet;

public class Structure extends HashSet<BlockPos> {
    private boolean valid = true;

    public Structure(int structureSize) {
        super(structureSize);
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
