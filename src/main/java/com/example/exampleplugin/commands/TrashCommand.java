package com.example.exampleplugin.commands;

import com.example.exampleplugin.ExamplePlugin;
import com.example.exampleplugin.ui.TrashCommandUI;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public class TrashCommand {
    public static List<LiteralCommandNode<CommandSourceStack>> commands = List.of(
            buildTrashLiteral("example:trash").build(),
            buildTrashLiteral("trash").build()
    );

    private static LiteralArgumentBuilder<CommandSourceStack> buildTrashLiteral(String name) {
        return Commands.literal(name)
                .requires(source -> source.getSender().hasPermission("example.trash"))
                .executes(context -> openTrashMenu(context.getSource()));
    }

    private static int openTrashMenu(CommandSourceStack stack) {
        try {
            Player player = stack.getPlayerOrThrow();
            player.openInventory(new TrashCommandUI().getInventory());

            return 1;
        } catch (CommandSyntaxException exception) {
            stack.sendFailure(Component.text(ExamplePlugin.config.playerOnlyCommandMessage()));
            return 0;
        } catch (Exception exception) {
            stack.sendFailure(Component.text(ExamplePlugin.config.menuOpenFailureMessage()));
            ExamplePlugin.logger.error("Failed to run trash command due to {}", exception.getMessage());
            return 0;
        }
    }
}
