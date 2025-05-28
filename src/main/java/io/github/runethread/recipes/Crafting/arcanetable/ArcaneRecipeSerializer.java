package io.github.runethread.recipes.Crafting.arcanetable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.runethread.recipes.RecipeResult;
import io.github.runethread.recipes.smelting.Philosophal;
import io.github.runethread.recipes.smelting.animator.AnimatorRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ArcaneRecipeSerializer implements RecipeSerializer<ArcaneRecipeShaped> {
    public static final MapCodec<ArcaneRecipeShaped> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(ArcaneRecipeShaped::getIngredients),
            RecipeResult.CODEC.listOf().fieldOf("results").forGetter(ArcaneRecipeShaped::getResultStack),
            Codec.INT.fieldOf("width").forGetter(ArcaneRecipeShaped::getWidth),
            Codec.INT.fieldOf("height").forGetter(ArcaneRecipeShaped::getHeight)
    ).apply(inst, ArcaneRecipeShaped::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ArcaneRecipeShaped> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.<RegistryFriendlyByteBuf, Ingredient>list().apply(Ingredient.CONTENTS_STREAM_CODEC), ArcaneRecipeShaped::getIngredients,
                    ByteBufCodecs.<RegistryFriendlyByteBuf, RecipeResult>list().apply(RecipeResult.STREAM_CODEC), ArcaneRecipeShaped::getResultStack,
                    ByteBufCodecs.INT, ArcaneRecipeShaped::getWidth,
                    ByteBufCodecs.INT, ArcaneRecipeShaped::getHeight,
                    ArcaneRecipeShaped::new
            );
    @Override
    public MapCodec<ArcaneRecipeShaped> codec() { return CODEC; }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ArcaneRecipeShaped> streamCodec() { return STREAM_CODEC; }
}