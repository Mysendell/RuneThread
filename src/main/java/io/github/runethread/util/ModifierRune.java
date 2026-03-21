package io.github.runethread.util;

public enum ModifierRune {
    LIVING_RUNE("Living Rune"),
    UP_RUNE("Up Rune"),
    DOWN_RUNE("Down Rune"),
    LEFT_RUNE("Left Rune"),
    RIGHT_RUNE("Right Rune"),
    NULL_RUNE("Null Rune"),
    REVERSE_RUNE("Reverse Rune"),
    SCALE_RUNE("Scale Rune"),
    SPHERE_RUNE("Sphere Rune"),
    CIRCLE_RUNE("Circle Rune"),
    SQUARE_RUNE("Square Rune");

    private final String name;
    ModifierRune(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
