package io.github.runethread.recipes.smelting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.runethread.recipes.Crafting.arcanetable.ArcaneRecipeShaped;
import io.github.runethread.recipes.RecipeResult;
import io.github.runethread.recipes.smelting.animator.AnimatorRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class PhilosophalSerializer  implements RecipeSerializer<Philosophal> {
    public static final MapCodec<Philosophal> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(Philosophal::getIngredients),
            RecipeResult.CODEC.listOf().fieldOf("results").forGetter(Philosophal::getResultStack),
            Codec.INT.fieldOf("width").forGetter(Philosophal::getWidth),
            Codec.INT.fieldOf("height").forGetter(Philosophal::getHeight),
            Codec.INT.fieldOf("burnTime").forGetter(Philosophal::getBurnTime),
            Codec.INT.fieldOf("fuelBurnMultiplier").forGetter(Philosophal::getFuelBurnMultiplier)
    ).apply(inst, Philosophal::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Philosophal> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.<RegistryFriendlyByteBuf, Ingredient>list().apply(Ingredient.CONTENTS_STREAM_CODEC), Philosophal::getIngredients,
                    ByteBufCodecs.<RegistryFriendlyByteBuf, RecipeResult>list().apply(RecipeResult.STREAM_CODEC), Philosophal::getResultStack,

                    ByteBufCodecs.INT, Philosophal::getWidth,
                    ByteBufCodecs.INT, Philosophal::getHeight,
                    ByteBufCodecs.INT, Philosophal::getBurnTime,
                    ByteBufCodecs.INT, Philosophal::getFuelBurnMultiplier,
                    Philosophal::new
            );


    @Override
    public MapCodec<Philosophal> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, Philosophal> streamCodec() {
        return STREAM_CODEC;
    }
}
