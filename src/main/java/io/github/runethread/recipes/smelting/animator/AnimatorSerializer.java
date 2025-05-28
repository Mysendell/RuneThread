package io.github.runethread.recipes.smelting.animator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.runethread.recipes.RecipeResult;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class AnimatorSerializer implements RecipeSerializer<AnimatorRecipe> {
    public static final MapCodec<AnimatorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(AnimatorRecipe::getIngredients),
            RecipeResult.CODEC.listOf().fieldOf("results").forGetter(AnimatorRecipe::getResultStack),
            Codec.INT.fieldOf("width").forGetter(AnimatorRecipe::getWidth),
            Codec.INT.fieldOf("height").forGetter(AnimatorRecipe::getHeight),
            Codec.INT.fieldOf("burnTime").forGetter(AnimatorRecipe::getBurnTime),
            Codec.INT.fieldOf("fuelBurnMultiplier").forGetter(AnimatorRecipe::getFuelBurnMultiplier)
    ).apply(inst, AnimatorRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AnimatorRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.<RegistryFriendlyByteBuf, Ingredient>list().apply(Ingredient.CONTENTS_STREAM_CODEC), AnimatorRecipe::getIngredients,
                    ByteBufCodecs.<RegistryFriendlyByteBuf, RecipeResult>list().apply(RecipeResult.STREAM_CODEC), AnimatorRecipe::getResultStack,
                    ByteBufCodecs.INT, AnimatorRecipe::getWidth,
                    ByteBufCodecs.INT, AnimatorRecipe::getHeight,
                    ByteBufCodecs.INT, AnimatorRecipe::getBurnTime,
                    ByteBufCodecs.INT, AnimatorRecipe::getFuelBurnMultiplier,
                    AnimatorRecipe::new
            );


    @Override
    public MapCodec<AnimatorRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, AnimatorRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
