package io.github.runethread.recipes;

import io.github.runethread.RuneThread;
import io.github.runethread.recipes.Crafting.ModRecipeSerializer;
import io.github.runethread.recipes.Crafting.RecipeShaped;
import io.github.runethread.recipes.Crafting.RecipeShapeless;
import io.github.runethread.recipes.smelting.PhilosophalRecipe;
import io.github.runethread.recipes.smelting.PhilosophalSerializer;
import io.github.runethread.recipes.smelting.SmeltingRecipe;
import io.github.runethread.recipes.smelting.SmeltingSerializer;
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

    public static final Supplier<RecipeType<CraftingRecipe>> RECIPE_SHAPED =
            RECIPE_TYPES.register("recipe_shaped",
                    name -> new RecipeType<CraftingRecipe>() {
                        @Override public String toString() { return name.toString(); }
                    });

    public static final Supplier<RecipeType<CraftingRecipe>> RECIPE_SHAPELESS =
            RECIPE_TYPES.register("recipe_shapeless",
                    name -> new RecipeType<CraftingRecipe>() {
                        @Override public String toString() { return name.toString(); }
                    });

    public static final Supplier<RecipeSerializer<RecipeShaped>> RECIPE_SHAPED_SERIALIZER =
            RECIPE_SERIALIZERS.register("recipe_shaped", () -> new ModRecipeSerializer<>(RecipeShaped::new));

    public static final Supplier<RecipeSerializer<RecipeShapeless>> RECIPE_SHAPELESS_SERIALIZER =
            RECIPE_SERIALIZERS.register("recipe_shapeless", () -> new ModRecipeSerializer<>(RecipeShapeless::new));

    public static final Supplier<RecipeType<CraftingRecipe>> SMELTING =
            RECIPE_TYPES.register("smelting",
                    name -> new RecipeType<>() {
                        @Override public String toString() { return name.toString(); }
                    });
    public static final Supplier<RecipeSerializer<SmeltingRecipe>> SMELTING_SERIALIZER =
            RECIPE_SERIALIZERS.register("smelting", () -> new SmeltingSerializer<>(SmeltingRecipe::new));

    public static final Supplier<RecipeType<CraftingRecipe>> PHILOSOPHAL =
            RECIPE_TYPES.register("philosophal",
                    name -> new RecipeType<>() {
                        @Override public String toString() { return name.toString(); }
                    });
    public static final Supplier<RecipeSerializer<PhilosophalRecipe>> PHILOSOPHAL_SERIALIZER =
            RECIPE_SERIALIZERS.register("philosophal", () -> new PhilosophalSerializer<>(PhilosophalRecipe::new));
}