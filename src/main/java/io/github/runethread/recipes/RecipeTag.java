package io.github.runethread.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.runethread.util.OptionalIntStreamCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record RecipeTag(TagKey<Item> tag, int count) implements IRecipeIngredient {
    public static final MapCodec<RecipeTag> MAP_CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                    TagKey.codec(Registries.ITEM).fieldOf("value").forGetter(RecipeTag::tag),
                    Codec.INT.optionalFieldOf("count", 1).forGetter(RecipeTag::count)
            ).apply(inst, RecipeTag::new)
    );


    public static final Codec<RecipeTag> CODEC = MAP_CODEC.codec();

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeTag> STREAM_CODEC = StreamCodec.composite(
            TagKey.streamCodec(Registries.ITEM), RecipeTag::tag,
            OptionalIntStreamCodec.of(1), RecipeTag::count,
            RecipeTag::new
    );

    @Override
    public boolean test(ItemStack stack) {
        return stack.getCount() >= count && stack.is(tag);
    }

    @Override
    public ItemStack toStack() {
        return ItemStack.EMPTY;
    }
}
