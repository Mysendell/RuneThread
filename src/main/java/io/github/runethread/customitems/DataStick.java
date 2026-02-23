package io.github.runethread.customitems;

import io.github.runethread.util.ChatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DataStick extends Item {
    public DataStick(Properties properties) {
        super(properties);
    }

    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, LivingEntity interactionTarget, @NotNull InteractionHand usedHand) {
        Level level = interactionTarget.level();
        if(level.isClientSide)
            return InteractionResult.PASS;
        ServerLevel serverLevel = (ServerLevel) level;

        int id = interactionTarget.getId();
        UUID UUID = interactionTarget.getUUID();
        String name = interactionTarget.getName().getString();

        String playerName = player.getName().getString();

        ChatUtils.sendInfoMessagePlayer(playerName, "Entity name: " + name, serverLevel);
        ChatUtils.sendInfoMessagePlayer(playerName, "Entity id: " + id, serverLevel);
        ChatUtils.sendInfoMessagePlayer(playerName, "Entity uuid: " + UUID, serverLevel);

        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if(level.isClientSide)
            return InteractionResult.PASS;
        ServerLevel serverLevel = (ServerLevel) level;

        int id = player.getId();
        UUID UUID = player.getUUID();
        String name = player.getName().getString();

        ChatUtils.sendInfoMessagePlayer(name, "Entity name: " + name, serverLevel);
        ChatUtils.sendInfoMessagePlayer(name, "Entity id: " + id, serverLevel);
        ChatUtils.sendInfoMessagePlayer(name, "Entity uuid: " + UUID, serverLevel);

        return InteractionResult.SUCCESS_SERVER;
    }
}
