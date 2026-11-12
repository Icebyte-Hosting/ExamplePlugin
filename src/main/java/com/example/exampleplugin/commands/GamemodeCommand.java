package com.example.exampleplugin.commands;

import com.example.exampleplugin.ExamplePlugin;
import com.example.exampleplugin.util.MessageParser;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;

public class GamemodeCommand {
    public static List<LiteralCommandNode<CommandSourceStack>> commands = List.of(
            buildGamemodeLiteral("example:gamemode").build(),
            buildGamemodeLiteral("gamemode").build()
    );

    private static LiteralArgumentBuilder<CommandSourceStack> buildGamemodeLiteral(String name) {
        return Commands.literal(name)
                .requires(source -> source.getSender().hasPermission("example.gamemode"))
                .then(Commands.argument("type", ArgumentTypes.gameMode())
                        .executes(context -> {
                            GameMode gameMode = context.getArgument("type", GameMode.class);
                            return updateGamemode(context.getSource(), gameMode);
                        })
                .then(Commands.argument("player", ArgumentTypes.player())
                        .executes(context -> {
                            var playerResolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                            GameMode gameMode = context.getArgument("type", GameMode.class);
                            Player player = playerResolver.resolve(context.getSource()).getFirst();
                            return updateGamemode(context.getSource(), gameMode, player);
                        })
                ));
    }

    private static int updateGamemode(CommandSourceStack stack, GameMode mode) {
        try {
            Player player = stack.getPlayerOrThrow();
            player.setGameMode(mode);

            stack.sendSuccess(Component.text(MessageParser.parseMessage(
                    ExamplePlugin.config.setPersonalModeMessage(), player, null, mode
            )).color(NamedTextColor.GREEN), true);

            return 1;
        } catch (CommandSyntaxException e) {
            stack.sendFailure(Component.text(ExamplePlugin.config.playerOnlyCommandMessage()));
            return 0;
        }
    }

    private static int updateGamemode(CommandSourceStack stack, GameMode mode, Player targetPlayer) {
        try {
            Player player = stack.getPlayerOrThrow();
            targetPlayer.setGameMode(mode);

            if (!targetPlayer.isConnected()) {
                stack.sendFailure(Component.text(ExamplePlugin.config.playerInvalidMessage()).color(NamedTextColor.RED));
                return 0;
            }

            targetPlayer.setGameMode(mode);
            targetPlayer.sendMessage(Component.text(MessageParser.parseMessage(
                    ExamplePlugin.config.gameModeUpdatedExternally(), player, targetPlayer, mode
            )).color(NamedTextColor.GREEN));

            stack.sendSuccess(Component.text(MessageParser.parseMessage(
                    ExamplePlugin.config.updatedTargetGameMode(), player, targetPlayer, mode
            )).color(NamedTextColor.GREEN), true);

            return 1;
        } catch (CommandSyntaxException e) {
            if (!targetPlayer.isConnected()) {
                stack.sendFailure(Component.text(ExamplePlugin.config.playerInvalidMessage()).color(NamedTextColor.RED));
                return 0;
            }

            targetPlayer.setGameMode(mode);
            targetPlayer.sendMessage(Component.text(MessageParser.parseMessage(
                    ExamplePlugin.config.gameModeUpdatedExternally(), "Console", targetPlayer, mode
            )).color(NamedTextColor.GREEN));

            stack.sendSuccess(Component.text(MessageParser.parseMessage(
                    ExamplePlugin.config.updatedTargetGameMode(), "Console", targetPlayer, mode
            )).color(NamedTextColor.GREEN), true);

            return 1;
        }
    }
}
