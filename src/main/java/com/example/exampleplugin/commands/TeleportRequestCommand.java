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
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class TeleportRequestCommand {
    public static List<LiteralCommandNode<CommandSourceStack>> commands = List.of(
            buildTeleportRequestLiteral("example:tpa").build(),
            buildTeleportRequestLiteral("tpa").build()
    );

    private static LiteralArgumentBuilder<CommandSourceStack> buildTeleportRequestLiteral(String name) {
        return Commands.literal(name)
                .requires(source -> source.getSender().hasPermission("example.tpa"))
                .then(Commands.argument("player", ArgumentTypes.player())
                        .executes(context -> {
                            var argResolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                            Player player = argResolver.resolve(context.getSource()).getFirst();
                            return teleportRequest(context.getSource(), player);
                        })
                );
    }

    private static int teleportRequest(CommandSourceStack stack, Player targetPlayer) {
        try {
            Player sourcePlayer = stack.getPlayerOrThrow();

            if (targetPlayer == null || !targetPlayer.isConnected()) {
                stack.sendFailure(Component.text(ExamplePlugin.config.playerInvalidMessage()).color(NamedTextColor.RED));
                return 0;
            }

            if (targetPlayer.getUniqueId().equals(sourcePlayer.getUniqueId())) {
                stack.sendFailure(Component.text(ExamplePlugin.config.tpaToSelfErrorMessage()).color(NamedTextColor.RED));
                return 0;
            }

            targetPlayer.sendMessage(makeTeleportRequest(sourcePlayer, targetPlayer));
            sourcePlayer.sendMessage(Component.text(MessageParser.parseMessage(
                    ExamplePlugin.config.tpaSentRequestMessage(), sourcePlayer, targetPlayer
            )).color(NamedTextColor.GREEN));

            return 1;
        } catch (CommandSyntaxException exception) {
            stack.sendFailure(Component.text(ExamplePlugin.config.playerOnlyCommandMessage()));
            return 0;
        } catch (Exception exception) {
            stack.sendFailure(Component.text(ExamplePlugin.config.tpaFailedRequestMessage()));
            ExamplePlugin.logger.error("Failed to run tpa command due to {}", exception.getMessage());
            return 0;
        }
    }

    private static Component makeTeleportRequest(Player sourcePlayer, Player targetPlayer) {
        Component message = Component.text(MessageParser.parseMessage(
                ExamplePlugin.config.tpaReceivedRequestMessage(),  sourcePlayer, targetPlayer
        )).color(NamedTextColor.YELLOW);

        Component acceptButton = Component.text("Teleport").color(NamedTextColor.GREEN)
                .clickEvent(ClickEvent.callback(_ -> {
                    targetPlayer.sendMessage(Component.text(MessageParser.parseMessage(
                            ExamplePlugin.config.tpaAcceptMessage(), sourcePlayer, targetPlayer
                    )).color(NamedTextColor.GREEN));

                    sourcePlayer.sendMessage(Component.text(MessageParser.parseMessage(
                            ExamplePlugin.config.tpaTargetAcceptedMessage(), sourcePlayer, targetPlayer
                    )).color(NamedTextColor.GREEN));

                    runTeleportTask(sourcePlayer, targetPlayer);
                }));

        Component declineButton = Component.text("Decline").color(NamedTextColor.RED)
                .clickEvent(ClickEvent.callback(_ -> {
                    targetPlayer.sendMessage(Component.text(MessageParser.parseMessage(
                            ExamplePlugin.config.tpaDeclineMessage(), sourcePlayer, targetPlayer
                    )).color(NamedTextColor.YELLOW));

                    sourcePlayer.sendMessage(Component.text(MessageParser.parseMessage(
                            ExamplePlugin.config.tpaTargetDeclinedMessage(), sourcePlayer, targetPlayer
                    )).color(NamedTextColor.RED));
                }));

        return Component.text()
                .append(message)
                .appendNewline()
                .append(acceptButton).appendSpace().append(declineButton)
                .build();
    }

    private static void runTeleportTask(Player sourcePlayer, Player targetPlayer) {
        Bukkit.getScheduler().runTaskAsynchronously(ExamplePlugin.plugin, () -> {
            for (int i = 3; i > 0; i--) {
                sourcePlayer.sendMessage(Component.text(MessageParser.parseMessage(
                        ExamplePlugin.config.tpaCountdownMessage(), sourcePlayer, targetPlayer, i
                )).color(NamedTextColor.YELLOW));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            Bukkit.getScheduler().runTask(ExamplePlugin.plugin, () -> sourcePlayer.teleport(targetPlayer));
        });
    }
}
