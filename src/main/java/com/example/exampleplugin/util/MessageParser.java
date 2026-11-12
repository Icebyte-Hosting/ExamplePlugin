package com.example.exampleplugin.util;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

public class MessageParser {
    public static String parseMessage(String message, @Nullable Player sourcePlayer, @Nullable Player targetPlayer) {
        String newMessage = message;

        if (sourcePlayer != null) newMessage = newMessage.replace("{sourcePlayer}", sourcePlayer.getName());
        if (targetPlayer != null) newMessage = newMessage.replace("{targetPlayer}", targetPlayer.getName());

        return newMessage;
    }

    public static String parseMessage(String message, @Nullable Player sourcePlayer, @Nullable Player targetPlayer, @Nullable Integer timeRemaining) {
        String newMessage = parseMessage(message, sourcePlayer, targetPlayer);
        if (timeRemaining != null) newMessage = newMessage.replace("{timeRemaining}", timeRemaining.toString());

        return newMessage;
    }

    public static String parseMessage(String message, @Nullable Player sourcePlayer, @Nullable Player targetPlayer, @Nullable GameMode gameMode) {
        String newMessage = parseMessage(message, sourcePlayer, targetPlayer);
        if (gameMode != null) newMessage = newMessage.replace("{gameMode}", gameMode.toString());

        return newMessage;
    }

    public static String parseMessage(String message, @Nullable String sourcePlayer, @Nullable Player targetPlayer, @Nullable GameMode gameMode) {
        String newMessage = message;

        if (sourcePlayer != null) newMessage = newMessage.replace("{sourcePlayer}", sourcePlayer);
        if (targetPlayer != null) newMessage = newMessage.replace("{targetPlayer}", targetPlayer.getName());
        if (gameMode != null) newMessage = newMessage.replace("{gameMode}", gameMode.toString());

        return newMessage;
    }
}
