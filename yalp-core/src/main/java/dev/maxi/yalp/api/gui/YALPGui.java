package dev.maxi.yalp.api.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class YALPGui implements InventoryHolder {
    private final Inventory inventory;
    private final Map<Integer, GuiClickHandler> handlers;
    private final boolean cancelClicks;

    YALPGui(String title, int size, Map<Integer, ItemStack> items, Map<Integer, GuiClickHandler> handlers, boolean cancelClicks) {
        this.handlers = new HashMap<>(handlers);
        this.cancelClicks = cancelClicks;
        this.inventory = Bukkit.createInventory(this, normalizeSize(size), title);
        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            if (entry.getKey() >= 0 && entry.getKey() < inventory.getSize()) {
                inventory.setItem(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void open(Player player) {
        if (player != null) {
            player.openInventory(inventory);
        }
    }

    public void handle(InventoryClickEvent event) {
        if (cancelClicks) {
            event.setCancelled(true);
        }
        GuiClickHandler handler = handlers.get(event.getRawSlot());
        if (handler != null) {
            handler.onClick(event);
        }
    }

    public Map<Integer, GuiClickHandler> getHandlers() {
        return Collections.unmodifiableMap(handlers);
    }

    private static int normalizeSize(int size) {
        int clamped = Math.max(9, Math.min(54, size));
        return ((clamped + 8) / 9) * 9;
    }
}
