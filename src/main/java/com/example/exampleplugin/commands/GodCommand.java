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
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class GodCommand {
    public static List<LiteralCommandNode<CommandSourceStack>> commands = List.of(
            buildFixLiteral("example:god").build(),
            buildFixLiteral("god").build()
    );

    private static LiteralArgumentBuilder<CommandSourceStack> buildFixLiteral(String name) {
        return Commands.literal(name)
                .requires(source -> source.getSender().hasPermission("example.god"))
                .executes(context -> runGodModeCommand(context.getSource(), null))
                .then(Commands.argument("player", ArgumentTypes.player())
                        .executes(context -> {
                            var argResolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                            Player player = argResolver.resolve(context.getSource()).getFirst();
                            return runGodModeCommand(context.getSource(), player);
                        })
                );
    }

    private static int runGodModeCommand(CommandSourceStack stack, @Nullable Player targetPlayer) {
        try {
            Player sourcePlayer = stack.getPlayerOrThrow();

            if (targetPlayer != null) {
                boolean enabled = toggleGodMode(targetPlayer);
                if (enabled) stack.sendSuccess(Component.text(MessageParser.parseMessage(
                        ExamplePlugin.config.targetEnterGodModeMessage(), sourcePlayer, targetPlayer)).color(NamedTextColor.YELLOW));
                else stack.sendSuccess(Component.text(MessageParser.parseMessage(
                        ExamplePlugin.config.targetLeaveGodModeMessage(), null, targetPlayer)).color(NamedTextColor.YELLOW));
            } else {
                toggleGodMode(sourcePlayer);
            }

            return 1;
        } catch (CommandSyntaxException exception) {
            if (targetPlayer == null) {
                stack.sendFailure(Component.text(ExamplePlugin.config.playerArgumentRequiredMessage()));
                return 0;
            }

            boolean enabled = toggleGodMode(targetPlayer);
            if (enabled) stack.sendSuccess(Component.text(MessageParser.parseMessage(
                    ExamplePlugin.config.targetEnterGodModeMessage(), null, targetPlayer)).color(NamedTextColor.YELLOW));
            else stack.sendSuccess(Component.text(MessageParser.parseMessage(
                    ExamplePlugin.config.targetLeaveGodModeMessage(), null, targetPlayer)).color(NamedTextColor.YELLOW));

            return 1;
        } catch (Exception exception) {
            stack.sendFailure(Component.text(ExamplePlugin.config.godModeSetFailedMessage()));
            ExamplePlugin.logger.error("Failed to run fix command due to {}", exception.getMessage());
            return 0;
        }
    }

    private static boolean toggleGodMode(Player player) {
        if (ExamplePlugin.godModePlayers.contains(player)) {
            ExamplePlugin.godModePlayers.remove(player);
            player.sendMessage(Component.text(ExamplePlugin.config.godModeDisabledMessage()).color(NamedTextColor.YELLOW));
            return false;
        } else {
            ExamplePlugin.godModePlayers.add(player);
            player.sendMessage(Component.text(ExamplePlugin.config.godModeEnabledMessage()).color(NamedTextColor.YELLOW));
            return true;
        }
    }
}
