package io.github.runethread.recipes;

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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class ArcaneRecipeSerializer implements RecipeSerializer<ArcaneRecipeShaped> {
    /*public static final MapCodec<ArcaneRecipeShaped> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(ArcaneRecipeShaped::getId),
            Codec.INT.fieldOf("width").forGetter(ArcaneRecipeShaped::getWidth),
            Codec.INT.fieldOf("height").forGetter(ArcaneRecipeShaped::getHeight),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(ArcaneRecipeShaped::inputItem),
            ItemStack.CODEC.fieldOf("result").forGetter(ArcaneRecipeShaped::getResult)
    ).apply(inst, (id, width, height, ingredients, result) ->
            new ArcaneRecipeShaped(id, ingredients, result, width, height)
    ));*/

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

  /*
    public static final StreamCodec<RegistryFriendlyByteBuf, List<Ingredient>> INGREDIENT_LIST_STREAM_CODEC = StreamCodec.of(
            (buf, list) -> {
                buf.writeVarInt(list.size());
                for (Ingredient ingredient : list) {
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
                }
            },
            buf -> {
                int size = buf.readVarInt();
                List<Ingredient> list = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    list.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
                }
                return list;
            }
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ArcaneRecipeShaped> STREAM_CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC, ArcaneRecipeShaped::getId,
                    net.minecraft.network.codec.ByteBufCodecs.VAR_INT, ArcaneRecipeShaped::getWidth,
                    net.minecraft.network.codec.ByteBufCodecs.VAR_INT, ArcaneRecipeShaped::getHeight,
                    INGREDIENT_LIST_STREAM_CODEC, ArcaneRecipeShaped::getIngredients,
                    ItemStack.STREAM_CODEC, ArcaneRecipeShaped::getResult,
                    (id, width, height, ingredients, result) -> new ArcaneRecipeShaped(id, ingredients, result, width, height)
            );*/

    @Override
    public MapCodec<ArcaneRecipeShaped> codec() { return CODEC; }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ArcaneRecipeShaped> streamCodec() { return STREAM_CODEC; }
}