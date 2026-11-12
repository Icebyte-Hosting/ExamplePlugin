package com.example.exampleplugin;

import com.example.exampleplugin.commands.*;
import com.example.exampleplugin.commands.*;
import com.example.exampleplugin.util.Config;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public final class ExamplePlugin extends JavaPlugin {
    public static ExamplePlugin plugin;
    public static Logger logger = LoggerFactory.getLogger(ExamplePlugin.class);
    public static ArrayList<Player> godModePlayers = new ArrayList<>();
    public static Config config;

    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();
        config = Config.fromYaml(getConfig());

        getServer().getPluginManager().registerEvents(new EventListener(), this);
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            TrashCommand.commands.forEach(command -> event.registrar().register(command));
            FixCommand.commands.forEach(command -> event.registrar().register(command));
            OpenInvCommand.commands.forEach(command -> event.registrar().register(command));
            EnderchestCommand.commands.forEach(command -> event.registrar().register(command));
            TeleportRequestCommand.commands.forEach(command -> event.registrar().register(command));
            GodCommand.commands.forEach(command -> event.registrar().register(command));
            GamemodeCommand.commands.forEach(command -> event.registrar().register(command));
        });
    }
}
