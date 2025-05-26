package io.github.runethread.recipes.arcanetable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ArcaneRecipeSerializer implements RecipeSerializer<ArcaneRecipeShaped> {
    public static final MapCodec<ArcaneRecipeShaped> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(ArcaneRecipeShaped::getId),
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(ArcaneRecipeShaped::getIngredients),
            ItemStack.CODEC.fieldOf("result").forGetter(ArcaneRecipeShaped::getResult),
            Codec.INT.fieldOf("width").forGetter(ArcaneRecipeShaped::getWidth),
            Codec.INT.fieldOf("height").forGetter(ArcaneRecipeShaped::getHeight)
    ).apply(inst, ArcaneRecipeShaped::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ArcaneRecipeShaped> STREAM_CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC, ArcaneRecipeShaped::getId,
                    ByteBufCodecs.<RegistryFriendlyByteBuf, Ingredient>list().apply(Ingredient.CONTENTS_STREAM_CODEC), ArcaneRecipeShaped::getIngredients,
                    ItemStack.STREAM_CODEC, ArcaneRecipeShaped::getResult,
                    ByteBufCodecs.INT, ArcaneRecipeShaped::getWidth,
                    ByteBufCodecs.INT, ArcaneRecipeShaped::getHeight,
                    ArcaneRecipeShaped::new
            );
    @Override
    public MapCodec<ArcaneRecipeShaped> codec() { return CODEC; }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ArcaneRecipeShaped> streamCodec() { return STREAM_CODEC; }
}