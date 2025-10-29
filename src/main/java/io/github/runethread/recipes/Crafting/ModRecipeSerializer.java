package io.github.runethread.recipes.Crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.runethread.recipes.IRecipeIngredient;
import io.github.runethread.recipes.RecipeIngredientSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public class ModRecipeSerializer<T extends ModRecipe> implements net.minecraft.world.item.crafting.RecipeSerializer<T> {
    private final MapCodec<T> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

    public ModRecipeSerializer(RecipeFactory<T> factory) {
        this.codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
                RecipeIngredientSerializer.CODEC.listOf().fieldOf("ingredients").forGetter(ModRecipe::getIngredients),
                RecipeIngredientSerializer.CODEC.listOf().fieldOf("results").forGetter(ModRecipe::getResults),
                Codec.INT.fieldOf("width").forGetter(ModRecipe::getRecipeWidth),
                Codec.INT.fieldOf("height").forGetter(ModRecipe::getRecipeHeight)
        ).apply(inst, factory::create));

        this.streamCodec = StreamCodec.composite(
                ByteBufCodecs.<RegistryFriendlyByteBuf, IRecipeIngredient>list().apply(RecipeIngredientSerializer.STREAM_CODEC), ModRecipe::getIngredients,
                ByteBufCodecs.<RegistryFriendlyByteBuf, IRecipeIngredient>list().apply(RecipeIngredientSerializer.STREAM_CODEC), ModRecipe::getResults,
                ByteBufCodecs.INT, ModRecipe::getRecipeWidth,
                ByteBufCodecs.INT, ModRecipe::getRecipeHeight,
                factory::create
        );
    }

    @Override
    public MapCodec<T> codec() { return codec; }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() { return streamCodec; }

    public interface RecipeFactory<T> {
        T create(List<IRecipeIngredient> ingredients, List<IRecipeIngredient> results, int width, int height);
    }
}