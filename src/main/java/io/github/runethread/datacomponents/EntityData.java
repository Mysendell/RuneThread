package io.github.runethread.datacomponents;

import io.github.runethread.util.ILocation;
import io.github.runethread.util.exceptions.GenericRuneException;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;


public record EntityData(UUID uuid, String name, ResourceKey<Level> dimension) implements ILocation {

    @Override
    public void addToTooltip(Item.TooltipContext ctx, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.literal(name + "#" + uuid).withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public ResourceKey<Level> getDimension(ServerLevel level) {
        level = level.getServer().getLevel(dimension);
        assert level != null;
        if (level.getEntity(uuid) != null)
            return dimension;
        for (ServerLevel possibleLevel : level.getServer().getAllLevels()) {
            if (possibleLevel.getEntity(uuid) != null)
                return possibleLevel.dimension();
        }
        throw new GenericRuneException("Ritual failed: Could not find reference!");
    }

    @Override
    public BlockPos getLocation(ServerLevel level) {
        return entity(level).blockPosition();
    }

    public Entity entity(ServerLevel level) {
        level = level.getServer().getLevel(dimension);
        assert level != null;
        try {
            return Objects.requireNonNull(level.getEntity(uuid));
        } catch (NullPointerException e) {
            for (ServerLevel possibleLevel : level.getServer().getAllLevels()) {
                if (possibleLevel.getEntity(uuid) != null)
                    return possibleLevel.getEntity(uuid);
            }
            throw e;
        }
    }

    public String uuidString() {
        return uuid.toString();
    }

    public ResourceLocation dimensionLocation() {
        return dimension.location();
    }

    public EntityData(String uuid, String name, ResourceLocation dimensionLocation) {
        this(
                UUID.fromString(uuid),
                name,
                ResourceKey.create(Registries.DIMENSION, dimensionLocation)
        );

    }
}
