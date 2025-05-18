package io.github.runethread.recipes;

// CustomRecipeSerializers.java

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CustomRecipeSerializer {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, "yourmodid");

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CustomShapedRecipe>> SHAPED =
            SERIALIZERS.register("custom_shaped", CustomShapedRecipeSerializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CustomShapelessRecipe>> SHAPELESS =
            SERIALIZERS.register("custom_shapeless", CustomShapelessRecipeSerializer::new);
}

