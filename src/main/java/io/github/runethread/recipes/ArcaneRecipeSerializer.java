package io.github.runethread.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.List;

public class ArcaneRecipeSerializer implements RecipeSerializer<ArcaneRecipeShaped> {
    // Codec for data pack/JSON
    public static final MapCodec<ArcaneRecipeShaped> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.fieldOf("width").forGetter(ArcaneRecipeShaped::getWidth),
            Codec.INT.fieldOf("height").forGetter(ArcaneRecipeShaped::getHeight),
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(ArcaneRecipeShaped::getIngredients),
            ItemStack.CODEC.fieldOf("result").forGetter(ArcaneRecipeShaped::getResult),
            Codec.INT.fieldOf("time").forGetter(ArcaneRecipeShaped::getCraftTime)
    ).apply(inst, (width, height, ingredients, result, time) ->
            new ArcaneRecipeShaped(null, ingredients, result, time, width, height)
    ));

    // StreamCodec for networking
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
                    net.minecraft.network.codec.ByteBufCodecs.VAR_INT, ArcaneRecipeShaped::getWidth,
                    net.minecraft.network.codec.ByteBufCodecs.VAR_INT, ArcaneRecipeShaped::getHeight,
                    INGREDIENT_LIST_STREAM_CODEC, ArcaneRecipeShaped::getIngredients,
                    ItemStack.STREAM_CODEC, ArcaneRecipeShaped::getResult,
                    net.minecraft.network.codec.ByteBufCodecs.VAR_INT, ArcaneRecipeShaped::getCraftTime,
                    (width, height, ingredients, result, time) -> new ArcaneRecipeShaped(null, ingredients, result, time, width, height)
            );

    @Override
    public MapCodec<ArcaneRecipeShaped> codec() { return CODEC; }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ArcaneRecipeShaped> streamCodec() { return STREAM_CODEC; }
}