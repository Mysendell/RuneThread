package io.github.runethread.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ArcaneRecipeSerializer implements RecipeSerializer<ArcaneRecipeShaped> {
    public static final MapCodec<ArcaneRecipeShaped> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(ArcaneRecipeShaped::getIngredients),
            ItemStack.CODEC.xmap(
                    stack -> (RegistryAccess access) -> stack,
                    func -> func.apply(null)
            ).fieldOf("result").forGetter(ArcaneRecipeShaped::getResultFunction),
            Codec.INT.fieldOf("time").forGetter(ArcaneRecipeShaped::getCraftTime)
    ).apply(inst, ArcaneRecipeShaped::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ArcaneRecipeShaped> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.COUNTED_CODEC.listOf().fieldOf("ingredients"), ArcaneRecipeShaped::getIngredients,
                    ItemStack.STREAM_CODEC, ArcaneRecipeShaped::getResultItem,
                    Codec.INT, ArcaneRecipeShaped::getCraftTime,
                    ArcaneRecipeShaped::new
            );

    @Override public MapCodec<ArcaneRecipeShaped> codec() { return CODEC; }
    @Override public StreamCodec<RegistryFriendlyByteBuf, ArcaneRecipeShaped> streamCodec() { return STREAM_CODEC; }
}
