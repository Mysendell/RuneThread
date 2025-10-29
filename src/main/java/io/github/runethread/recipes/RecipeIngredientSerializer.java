package io.github.runethread.recipes;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Serializer for recipe ingredients that can be either an item or a tag.
 */
public class RecipeIngredientSerializer {
    public static final Codec<IRecipeIngredient> CODEC =
            Codec.STRING.dispatch(
                            // discriminator extractor from the IRecipeIngredient
                            ingredient -> {
                                if (ingredient instanceof RecipeItem) return "item";
                                if (ingredient instanceof RecipeTag) return "tag";
                                throw new IllegalStateException("Unknown ingredient subtype: " + ingredient);
                            },
                            // map from discriminator to subtype-mapcodec
                            type -> {
                                switch (type) {
                                    case "item": return RecipeItem.MAP_CODEC;
                                    case "tag": return RecipeTag.MAP_CODEC;
                                    default: throw new IllegalArgumentException("Unknown type: " + type);
                                }
                            }
                    );


    private static final ResourceKey<Registry<Object>> DISPATCH = ResourceKey.createRegistryKey(
            ResourceLocation.fromNamespaceAndPath("runethread", "recipe_ingredient_stream_codecs")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, IRecipeIngredient> STREAM_CODEC =
            ByteBufCodecs.registry(DISPATCH).dispatch(
                    ingredient -> {
                        if (ingredient instanceof RecipeItem) return "item";
                        if (ingredient instanceof RecipeTag) return "tag";
                        throw new IllegalStateException("Unknown ingredient type: " + ingredient);
                    },

                    type -> switch ((String) type) {
                        case "item" -> RecipeItem.STREAM_CODEC;
                        case "tag"  -> RecipeTag.STREAM_CODEC;
                        default -> throw new IllegalArgumentException("Unknown ingredient type: " + type);
                    }
            );
}