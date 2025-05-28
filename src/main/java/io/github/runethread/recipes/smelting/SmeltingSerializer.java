package io.github.runethread.recipes.smelting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.runethread.recipes.RecipeResult;
import io.github.runethread.recipes.smelting.animator.AnimatorRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class SmeltingSerializer  implements RecipeSerializer<Smelting> {
    public static final MapCodec<Smelting> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(Smelting::getIngredients),
            RecipeResult.CODEC.listOf().fieldOf("results").forGetter(Smelting::getResultStack),
            Codec.INT.fieldOf("width").forGetter(Smelting::getWidth),
            Codec.INT.fieldOf("height").forGetter(Smelting::getHeight),
            Codec.INT.fieldOf("burnTime").forGetter(Smelting::getBurnTime),
            Codec.INT.fieldOf("fuelBurnMultiplier").forGetter(Smelting::getFuelBurnMultiplier)
    ).apply(inst, Smelting::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Smelting> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.<RegistryFriendlyByteBuf, Ingredient>list().apply(Ingredient.CONTENTS_STREAM_CODEC), Smelting::getIngredients,
                    ByteBufCodecs.<RegistryFriendlyByteBuf, RecipeResult>list().apply(RecipeResult.STREAM_CODEC), Smelting::getResultStack,
                    ByteBufCodecs.INT, Smelting::getWidth,
                    ByteBufCodecs.INT, Smelting::getHeight,
                    ByteBufCodecs.INT, Smelting::getBurnTime,
                    ByteBufCodecs.INT, Smelting::getFuelBurnMultiplier,
                    Smelting::new
            );
    
    
    @Override
    public MapCodec<Smelting> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, Smelting> streamCodec() {
        return STREAM_CODEC;
    }
}
