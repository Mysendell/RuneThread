package io.github.runethread.recipes;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;

public class ListStreamCodecs {
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
}