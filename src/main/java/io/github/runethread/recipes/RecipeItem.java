package io.github.runethread.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
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
public record RecipeItem(Item item, int count) implements IRecipeIngredient{

    public static final MapCodec<RecipeItem> MAP_CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("value").forGetter(RecipeItem::item),
                    Codec.INT.optionalFieldOf("count", 1).forGetter(RecipeItem::count)
            ).apply(inst, RecipeItem::new)
    );

    public static final Codec<RecipeItem> CODEC = MAP_CODEC.codec();

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeItem> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.ITEM), RecipeItem::item,
            OptionalIntStreamCodec.of(1), RecipeItem::count,
            RecipeItem::new
    );


    public ItemStack toStack() {
        return new ItemStack(item, count);
    }
    public boolean test(ItemStack stack) {
        return stack.getCount() >= count && stack.is(item);
    }
}
