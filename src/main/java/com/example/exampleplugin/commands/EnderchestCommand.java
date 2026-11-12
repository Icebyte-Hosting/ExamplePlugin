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
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class EnderchestCommand {
    public static List<LiteralCommandNode<CommandSourceStack>> commands = List.of(
            buildEnderchestLiteral("example:enderchest").build(),
            buildEnderchestLiteral("enderchest").build()
    );

    private static LiteralArgumentBuilder<CommandSourceStack> buildEnderchestLiteral(String name) {
        return Commands.literal(name)
                .requires(source -> source.getSender().hasPermission("example.enderchest"))
                .executes(context -> openEnderchest(context.getSource(), null))
                .then(Commands.argument("player", ArgumentTypes.player())
                        .executes(context -> {
                            var argResolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                            Player player = argResolver.resolve(context.getSource()).getFirst();
                            return openEnderchest(context.getSource(), player);
                        })
                );
    }

    private static int openEnderchest(CommandSourceStack stack, @Nullable Player targetPlayer) {
        try {
            Player sourcePlayer = stack.getPlayerOrThrow();
            Inventory enderchestInventory;

            if (targetPlayer != null) {
                if (!targetPlayer.isConnected()) {
                    stack.sendFailure(Component.text(ExamplePlugin.config.playerInvalidMessage()).color(NamedTextColor.RED));
                    return 0;
                }

                enderchestInventory = targetPlayer.getEnderChest();
            } else {
                enderchestInventory = sourcePlayer.getEnderChest();
            }

            sourcePlayer.openInventory(enderchestInventory);
            return 1;
        } catch (CommandSyntaxException exception) {
            stack.sendFailure(Component.text(ExamplePlugin.config.playerOnlyCommandMessage()));
            return 0;
        } catch (Exception exception) {
            stack.sendFailure(Component.text(ExamplePlugin.config.menuOpenFailureMessage()).color(NamedTextColor.RED));
            ExamplePlugin.logger.error("Failed to run enderchest command due to {}", exception.getMessage());
            return 0;
        }
    }
}
