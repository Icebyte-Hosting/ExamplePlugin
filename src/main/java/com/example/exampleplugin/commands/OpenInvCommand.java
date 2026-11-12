package com.example.exampleplugin.commands;

import com.example.exampleplugin.ExamplePlugin;
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

import java.util.List;

public class OpenInvCommand {
    public static List<LiteralCommandNode<CommandSourceStack>> commands = List.of(
            buildOpenInvLiteral("example:openinv").build(),
            buildOpenInvLiteral("openinv").build()
    );

    private static LiteralArgumentBuilder<CommandSourceStack> buildOpenInvLiteral(String name) {
        return Commands.literal(name)
                .requires(source -> source.getSender().hasPermission("example.openinv"))
                .then(Commands.argument("player", ArgumentTypes.player())
                        .executes(context -> {
                            var argResolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                            Player player = argResolver.resolve(context.getSource()).getFirst();
                            return openInv(context.getSource(), player);
                        })
                );
    }

    private static int openInv(CommandSourceStack stack, Player targetPlayer) {
        try {
            Player sourcePlayer = stack.getPlayerOrThrow();

            if (targetPlayer == null || !targetPlayer.isConnected()) {
                stack.sendFailure(Component.text(ExamplePlugin.config.playerInvalidMessage()).color(NamedTextColor.RED));
                return 0;
            }

            sourcePlayer.openInventory(targetPlayer.getInventory());
            return 1;
        } catch (CommandSyntaxException exception) {
            stack.sendFailure(Component.text(ExamplePlugin.config.playerOnlyCommandMessage()));
            return 0;
        } catch (Exception exception) {
            stack.sendFailure(Component.text(ExamplePlugin.config.menuOpenFailureMessage()));
            ExamplePlugin.logger.error("Failed to run openinv command due to {}", exception.getMessage());
            return 0;
        }
    }
}
