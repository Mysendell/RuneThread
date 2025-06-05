package io.github.runethread.recipes.smelting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.runethread.recipes.RecipeResult;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

public class PhilosophalSerializer<T extends Philosophal>  implements RecipeSerializer<T> {
    public final MapCodec<T> CODEC;

    public final StreamCodec<RegistryFriendlyByteBuf, T> STREAM_CODEC;


    public PhilosophalSerializer(RecipeFactory<T> factory) {
        CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(Philosophal::getIngredients),
                RecipeResult.CODEC.listOf().fieldOf("results").forGetter(Philosophal::getResultStack),
                Codec.INT.fieldOf("width").forGetter(Philosophal::getWidth),
                Codec.INT.fieldOf("height").forGetter(Philosophal::getHeight),
                Codec.INT.fieldOf("burnTime").forGetter(Philosophal::getBurnTime),
                Codec.INT.fieldOf("fuelBurnMultiplier").forGetter(Philosophal::getFuelBurnMultiplier)
        ).apply(inst, factory::create));

        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.<RegistryFriendlyByteBuf, Ingredient>list().apply(Ingredient.CONTENTS_STREAM_CODEC), Philosophal::getIngredients,
                ByteBufCodecs.<RegistryFriendlyByteBuf, RecipeResult>list().apply(RecipeResult.STREAM_CODEC), Philosophal::getResultStack,

                ByteBufCodecs.INT, Philosophal::getWidth,
                ByteBufCodecs.INT, Philosophal::getHeight,
                ByteBufCodecs.INT, Philosophal::getBurnTime,
                ByteBufCodecs.INT, Philosophal::getFuelBurnMultiplier,
                factory::create
        );
    }

    public interface RecipeFactory<T> {
        T create(List<Ingredient> ingredients, List<RecipeResult> results, int width, int height, int burnTime, int fuelBurnMultiplier);
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
