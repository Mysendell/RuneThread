package io.github.runethread.recipes;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class OptionalIntStreamCodec {
    public static StreamCodec<RegistryFriendlyByteBuf, Integer> of(int defaultValue) {
        return StreamCodec.of(
                (buf, value) -> {
                    boolean present = value != defaultValue;
                    buf.writeBoolean(present);
                    if (present) buf.writeVarInt(value);
                },
                buf -> {
                    boolean present = buf.readBoolean();
                    return present ? buf.readVarInt() : defaultValue;
                }
        );
    }
}