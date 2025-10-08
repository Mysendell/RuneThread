package io.github.runethread.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.runethread.util.OptionalIntStreamCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Represents the result of a recipe, including the item and the quantity produced.
 * <p>
 * This class provides serialization codecs for both JSON and network transmission.
 */
public record RecipeResult(Item item, int count) {

    public static final Codec<RecipeResult> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(RecipeResult::item),
            Codec.INT.optionalFieldOf("count", 1).forGetter(RecipeResult::count)
    ).apply(inst, RecipeResult::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeResult> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.registry(Registries.ITEM), RecipeResult::item,
                    OptionalIntStreamCodec.of(1), RecipeResult::count,
                    RecipeResult::new
            );

    public ItemStack toStack() {
        return new ItemStack(item, count);
    }
}
