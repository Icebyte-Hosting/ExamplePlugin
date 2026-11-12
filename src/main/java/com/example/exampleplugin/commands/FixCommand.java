package com.example.exampleplugin.commands;

import com.example.exampleplugin.ExamplePlugin;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class FixCommand {
    public static List<LiteralCommandNode<CommandSourceStack>> commands = List.of(
            buildFixLiteral("example:fix").build(),
            buildFixLiteral("fix").build()
    );

    private static LiteralArgumentBuilder<CommandSourceStack> buildFixLiteral(String name) {
        return Commands.literal(name)
                .requires(source -> source.getSender().hasPermission("example.fix"))
                .executes(context -> fixHandItem(context.getSource()));
    }

    private static int fixHandItem(CommandSourceStack stack) {
        try {
            Player player = stack.getPlayerOrThrow();
            ItemStack item = player.getInventory().getItemInMainHand();
            ItemMeta itemMeta = item.getItemMeta();

            if (itemMeta instanceof Damageable damageable) {
                if (damageable.getDamage() == 0) {
                    stack.sendFailure(Component.text(ExamplePlugin.config.itemAlreadyFullHealthMessage()).color(NamedTextColor.YELLOW));
                    return 0;
                }

                damageable.setDamage(0);
                item.setItemMeta(damageable);
                player.updateInventory();

                player.sendMessage(Component.text(ExamplePlugin.config.itemSuccessfullyRepairedMessage()).color(NamedTextColor.GREEN));
                return 1;
            } else {
                stack.sendFailure(Component.text(ExamplePlugin.config.itemNotDamageableMessage()).color(NamedTextColor.RED));
                return 0;
            }

        } catch (CommandSyntaxException exception) {
            stack.sendFailure(Component.text(ExamplePlugin.config.playerOnlyCommandMessage()));
            return 0;
        } catch (Exception exception) {
            stack.sendFailure(Component.text(ExamplePlugin.config.itemRepairFailedMessage()));
            ExamplePlugin.logger.error("Failed to run fix command due to {}", exception.getMessage());
            return 0;
        }
    }
}
