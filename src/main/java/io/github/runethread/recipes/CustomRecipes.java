package io.github.runethread.recipes;

import io.github.runethread.RuneThread;
import io.github.runethread.recipes.Crafting.ModRecipeSerializer;
import io.github.runethread.recipes.Crafting.arcanetable.ArcaneRecipeShaped;
import io.github.runethread.recipes.Crafting.arcanetable.ArcaneRecipeShapeless;
import io.github.runethread.recipes.smelting.Philosophal;
import io.github.runethread.recipes.smelting.PhilosophalSerializer;
import io.github.runethread.recipes.smelting.Smelting;
import io.github.runethread.recipes.smelting.SmeltingSerializer;
import io.github.runethread.recipes.smelting.animator.AnimatorRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CustomRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, RuneThread.MODID);

    public static final DeferredRegister<net.minecraft.world.item.crafting.RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, RuneThread.MODID);

    public static final DeferredRegister<RecipeBookCategory> RECIPE_BOOK_CATEGORIES =
            DeferredRegister.create(Registries.RECIPE_BOOK_CATEGORY, RuneThread.MODID);

    public static final Supplier<RecipeBookCategory> RUNETHREAD_CATEGORY = RECIPE_BOOK_CATEGORIES.register(
            "runethread", RecipeBookCategory::new
    );

    public static final Supplier<RecipeType<CraftingRecipe>> ARCANE_SHAPED =
            RECIPE_TYPES.register("arcane_shaped",
                    name -> new RecipeType<CraftingRecipe>() {
                        @Override public String toString() { return name.toString(); }
                    });
    
    public static final Supplier<RecipeType<CraftingRecipe>> ARCANE_SHAPELESS =
            RECIPE_TYPES.register("arcane_shapeless",
                    name -> new RecipeType<CraftingRecipe>() {
                        @Override public String toString() { return name.toString(); }
                    });

    public static final Supplier<RecipeSerializer<ArcaneRecipeShaped>> ARCANE_SHAPED_SERIALIZER =
            RECIPE_SERIALIZERS.register("arcane_shaped", () -> new ModRecipeSerializer<>(ArcaneRecipeShaped::new));

    public static final Supplier<RecipeSerializer<ArcaneRecipeShapeless>> ARCANE_SHAPELESS_SERIALIZER =
            RECIPE_SERIALIZERS.register("arcane_shapeless", () -> new ModRecipeSerializer<>(ArcaneRecipeShapeless::new));

    public static final Supplier<RecipeSerializer<AnimatorRecipe>> ANIMATOR_SERIALIZER =
            RECIPE_SERIALIZERS.register("animator", () -> new PhilosophalSerializer<>(AnimatorRecipe::new));

    public static final Supplier<RecipeType<CraftingRecipe>> SMELTING =
            RECIPE_TYPES.register("smelting",
                    name -> new RecipeType<>() {
                        @Override public String toString() { return name.toString(); }
                    });
    public static final Supplier<RecipeSerializer<Smelting>> SMELTING_SERIALIZER =
            RECIPE_SERIALIZERS.register("smelting", () -> new SmeltingSerializer<>(Smelting::new));

    public static final Supplier<RecipeType<CraftingRecipe>> PHILOSOPHAL =
            RECIPE_TYPES.register("philosophal",
                    name -> new RecipeType<>() {
                        @Override public String toString() { return name.toString(); }
                    });
    public static final Supplier<RecipeSerializer<Philosophal>> PHILOSOPHAL_SERIALIZER =
            RECIPE_SERIALIZERS.register("philosophal", () -> new PhilosophalSerializer<>(Philosophal::new));

    public static final Supplier<RecipeType<CraftingRecipe>> ANIMATOR_RECIPE =
            RECIPE_TYPES.register("animator",
                    name -> new RecipeType<>() {
                        @Override public String toString() { return name.toString(); }
                    });
}