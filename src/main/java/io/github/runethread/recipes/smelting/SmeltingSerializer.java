package io.github.runethread.recipes.smelting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.runethread.recipes.IRecipeIngredient;
import io.github.runethread.recipes.RecipeIngredientSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

public class SmeltingSerializer<T extends SmeltingRecipe>  implements RecipeSerializer<T> {
    public  final MapCodec<T> CODEC;

    public  final StreamCodec<RegistryFriendlyByteBuf, T> STREAM_CODEC;

    public SmeltingSerializer(RecipeFactory<T> factory) {
        CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                RecipeIngredientSerializer.CODEC.listOf().fieldOf("ingredients").forGetter(SmeltingRecipe::getIngredients),
                RecipeIngredientSerializer.CODEC.listOf().fieldOf("results").forGetter(SmeltingRecipe::getResults),
                Codec.INT.fieldOf("width").forGetter(SmeltingRecipe::getRecipeWidth),
                Codec.INT.fieldOf("height").forGetter(SmeltingRecipe::getRecipeHeight),
                Codec.INT.fieldOf("burnTime").forGetter(SmeltingRecipe::getBurnTime),
                Codec.INT.fieldOf("fuelBurnMultiplier").forGetter(SmeltingRecipe::getFuelBurnMultiplier)
        ).apply(inst, factory::create));

        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.<RegistryFriendlyByteBuf, IRecipeIngredient>list().apply(RecipeIngredientSerializer.STREAM_CODEC), SmeltingRecipe::getIngredients,
                ByteBufCodecs.<RegistryFriendlyByteBuf, IRecipeIngredient>list().apply(RecipeIngredientSerializer.STREAM_CODEC), SmeltingRecipe::getResults,
                ByteBufCodecs.INT, SmeltingRecipe::getRecipeWidth,
                ByteBufCodecs.INT, SmeltingRecipe::getRecipeHeight,
                ByteBufCodecs.INT, SmeltingRecipe::getBurnTime,
                ByteBufCodecs.INT, SmeltingRecipe::getFuelBurnMultiplier,
                factory::create
        );
    }

    public interface RecipeFactory<T> {
        T create(List<IRecipeIngredient> ingredients, List<IRecipeIngredient> results, int width, int height, int burnTime, int fuelBurnMultiplier);
    }
    
    @Override
    public MapCodec<T> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
        return STREAM_CODEC;
    }
}
