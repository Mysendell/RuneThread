package io.github.runethread.recipes.Crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.runethread.recipes.RecipeResult;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class ModRecipeSerializer<T extends Recipe> implements net.minecraft.world.item.crafting.RecipeSerializer<T> {
    private final MapCodec<T> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

    public ModRecipeSerializer(RecipeFactory<T> factory) {
        // Construct the CODEC and STREAM_CODEC using the factory
        this.codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(Recipe::getIngredients),
                RecipeResult.CODEC.listOf().fieldOf("results").forGetter(Recipe::getResultStack),
                Codec.INT.fieldOf("width").forGetter(Recipe::getWidth),
                Codec.INT.fieldOf("height").forGetter(Recipe::getHeight)
        ).apply(inst, factory::create));

        this.streamCodec = StreamCodec.composite(
                ByteBufCodecs.<RegistryFriendlyByteBuf, Ingredient>list().apply(Ingredient.CONTENTS_STREAM_CODEC), Recipe::getIngredients,
                ByteBufCodecs.<RegistryFriendlyByteBuf, RecipeResult>list().apply(RecipeResult.STREAM_CODEC), Recipe::getResultStack,
                ByteBufCodecs.INT, Recipe::getWidth,
                ByteBufCodecs.INT, Recipe::getHeight,
                factory::create
        );
    }

    @Override
    public MapCodec<T> codec() { return codec; }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() { return streamCodec; }

    // Factory for constructing recipe instances
    public interface RecipeFactory<T> {
        T create(List<Ingredient> ingredients, List<RecipeResult> results, int width, int height);
    }
}