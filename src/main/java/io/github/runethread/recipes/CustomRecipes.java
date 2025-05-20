package io.github.runethread.recipes;

import io.github.runethread.RuneThread;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CustomRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, RuneThread.MODID);

    public static final Supplier<RecipeType<ArcaneRecipeShaped>> MAGIC_RECIPE_TYPE =
            RECIPE_TYPES.register("arcane_shaped",
                    name -> new RecipeType<ArcaneRecipeShaped>() {
                        @Override public String toString() { return name.toString(); }
                    });

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, RuneThread.MODID);

    public static final Supplier<RecipeSerializer<ArcaneRecipeShaped>> MAGIC_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("arcane_shaped", ArcaneRecipeSerializer::new);

}
