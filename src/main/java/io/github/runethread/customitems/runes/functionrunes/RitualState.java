package io.github.runethread.customitems.runes.functionrunes;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum RitualState implements StringRepresentable {
    IDLE,
    NEUTRAL,
    SUCCESS,
    ALMOST,
    FAIL;

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase();
    }
}
