package io.github.runethread.recipes;

import io.github.runethread.RuneThread;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CustomRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, RuneThread.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<Recipe<?>>> SHAPED =
            TYPES.register("custom_shaped", () -> new RecipeType<>() {});

    public static final DeferredHolder<RecipeType<?>, RecipeType<Recipe<?>>> SHAPELESS =
            TYPES.register("custom_shapeless", () -> new RecipeType<>() {});
}
