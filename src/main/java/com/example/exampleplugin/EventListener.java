package com.example.exampleplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class EventListener implements Listener {
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (ExamplePlugin.godModePlayers.contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerHunger(FoodLevelChangeEvent  event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (ExamplePlugin.godModePlayers.contains(player)) {
            event.setCancelled(true);
        }
    }
}
