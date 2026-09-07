package io.github.runethread.util.runeutils;

import io.github.runethread.RuneThread;
import net.minecraft.resources.ResourceLocation;

public enum ModifierRune {
    LIVING_RUNE(ResourceLocation.fromNamespaceAndPath(RuneThread.MODID,"living_rune")),
    UP_RUNE(ResourceLocation.fromNamespaceAndPath(RuneThread.MODID,"up_rune")),
    DOWN_RUNE(ResourceLocation.fromNamespaceAndPath(RuneThread.MODID,"down_rune")),
    LEFT_RUNE(ResourceLocation.fromNamespaceAndPath(RuneThread.MODID,"left_une")),
    RIGHT_RUNE(ResourceLocation.fromNamespaceAndPath(RuneThread.MODID,"right_rune")),
    NULL_RUNE(ResourceLocation.fromNamespaceAndPath(RuneThread.MODID,"null_rune")),
    REVERSE_RUNE(ResourceLocation.fromNamespaceAndPath(RuneThread.MODID,"reverse_rune")),
    SCALE_RUNE(ResourceLocation.fromNamespaceAndPath(RuneThread.MODID,"scale_rune")),
    SPHERE_RUNE(ResourceLocation.fromNamespaceAndPath(RuneThread.MODID,"sphere_rune")),
    CIRCLE_RUNE(ResourceLocation.fromNamespaceAndPath(RuneThread.MODID,"circle_rune")),
    SQUARE_RUNE(ResourceLocation.fromNamespaceAndPath(RuneThread.MODID,"square_rune"));

    private final ResourceLocation name;
    ModifierRune(ResourceLocation name) {
        this.name = name;
    }

    public ResourceLocation getName() {
        return name;
    }
}
