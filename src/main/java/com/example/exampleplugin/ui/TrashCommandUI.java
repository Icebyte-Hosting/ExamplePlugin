package com.example.exampleplugin.ui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class TrashCommandUI implements InventoryHolder {
    private final Inventory trashInventory;

    public TrashCommandUI() {
        this.trashInventory = Bukkit.getServer().createInventory(
                this,
                54,
                Component.text("Trash Can")
        );
    }

    @Override
    public @NotNull Inventory getInventory() {
        return trashInventory;
    }
}
