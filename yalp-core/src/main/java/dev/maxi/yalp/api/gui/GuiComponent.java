package dev.maxi.yalp.api.gui;

import dev.maxi.yalp.api.component.ComponentContext;
import dev.maxi.yalp.api.component.YALPComponent;
import dev.maxi.yalp.api.message.MessagesComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class GuiComponent implements YALPComponent, Listener {
    private final Set<YALPGui> openGuis = Collections.synchronizedSet(new HashSet<>());
    private JavaPlugin plugin;
    private MessagesComponent messages;

    @Override
    public String getId() {
        return "gui";
    }

    @Override
    public List<String> getDependencies() {
        return Collections.singletonList("messages");
    }

    @Override
    public void onLoad(ComponentContext context) {
        this.plugin = context.getPlugin();
        this.messages = context.getApi().messages();
    }

    @Override
    public void onEnable() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void onDisable() {
        openGuis.clear();
    }

    public GuiBuilder create(String title, int size) {
        return new GuiBuilder(messages == null ? title : messages.color(title), size);
    }

    public void track(YALPGui gui) {
        if (gui != null) {
            openGuis.add(gui);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory != null && inventory.getHolder() instanceof YALPGui) {
            YALPGui gui = (YALPGui) inventory.getHolder();
            track(gui);
            gui.handle(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory != null && inventory.getHolder() instanceof YALPGui) {
            openGuis.remove(inventory.getHolder());
        }
    }
}
