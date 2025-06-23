package io.github.runethread.customeffects;

import io.github.runethread.RuneThread;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CustomEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, RuneThread.MODID);

    public static final DeferredHolder<MobEffect, MobEffect> IMMUNITY =
            EFFECTS.register("immunity", ImmunityEffect::new);
}
