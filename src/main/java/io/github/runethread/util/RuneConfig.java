package io.github.runethread.util;

import net.neoforged.neoforge.common.ModConfigSpec;

public class RuneConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue ANIMATION_EXAGGERATION =
            BUILDER.comment("Multiplier for animation exaggeration")
                    .defineInRange("animationExaggeration", 1.0, 0.0, 400.0);
    public static final ModConfigSpec.DoubleValue ANIMATION_TIME_MULTIPLIER =
            BUILDER.comment("Multiplier for animation walk cycle speed")
                    .defineInRange("animationTimeMultiplier", 1.0, 0.1, 500.0);
    public static final ModConfigSpec.DoubleValue ANIMATION_SPEED_MULTIPLIER =
            BUILDER.comment("Multiplier for animation walk cycle speed")
                    .defineInRange("animationSpeedMultiplier", 1.0, 0.1, 500.0);
    public static final ModConfigSpec.DoubleValue ANIMATION_SPEED =
            BUILDER.comment("Animation walk cycle speed")
                    .defineInRange("animationSpeed", 1.0, 0.1, 500.0);

    public static final ModConfigSpec SPEC = BUILDER.build();
}