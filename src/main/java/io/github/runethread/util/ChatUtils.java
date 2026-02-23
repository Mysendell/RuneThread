package io.github.runethread.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class ChatUtils {
    public static void sendErrorMessagePlayer(String playerName, String message, ServerLevel level) {
        sendMessagePlayer(playerName, Component.literal(message).withStyle(ChatFormatting.RED, ChatFormatting.BOLD), level);
    }
    public static void sendInfoMessagePlayer(String playerName, String message, ServerLevel level) {
        sendMessagePlayer(playerName, Component.literal(message).withStyle(ChatFormatting.BLUE), level);
    }
    public static void sendSuccessMessagePlayer(String playerName, String message, ServerLevel level) {
        sendMessagePlayer(playerName, Component.literal(message).withStyle(ChatFormatting.GREEN), level);
    }
    public static void sendMessagePlayer(String playerName, Component messageComponent, ServerLevel level) {
        ServerPlayer player = Objects.requireNonNull(level.getServer()).getPlayerList().getPlayerByName(playerName);
        if (player != null) {
            player.sendSystemMessage(messageComponent);
        }
    }

    public static void clearChat(Level level) {
        Objects.requireNonNull(level.getServer()).getPlayerList().broadcastSystemMessage(Component.empty(), false);
    }

    public static void sendErrorMessageAll(String message, Level level) {
        sendMessageAll(Component.literal(message).withStyle(ChatFormatting.RED, ChatFormatting.BOLD), level);
    }
    public static void sendInfoMessageAll(String message, Level level) {
        sendMessageAll(Component.literal(message).withStyle(ChatFormatting.BLUE), level);
    }
    public static void sendSuccessMessageAll(String message, Level level) {
        sendMessageAll(Component.literal(message).withStyle(ChatFormatting.GREEN), level);
    }
    public static void sendMessageAll(Component messageComponent, Level level) {
        Objects.requireNonNull(level.getServer()).getPlayerList().broadcastSystemMessage(messageComponent, false);
    }
}
