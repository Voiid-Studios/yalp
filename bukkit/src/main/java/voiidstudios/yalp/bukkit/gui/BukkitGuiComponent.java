package voiidstudios.yalp.bukkit.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import voiidstudios.yalp.bukkit.messages.BukkitMessages;
import voiidstudios.yalp.core.component.YALPComponent;

public final class BukkitGuiComponent implements YALPComponent, Listener {
    private final JavaPlugin plugin;
    private final BukkitMessages messages;

    public BukkitGuiComponent(JavaPlugin plugin, BukkitMessages messages) {
        this.plugin = plugin;
        this.messages = messages;
    }

    @Override
    public String getId() {
        return "gui";
    }

    @Override
    public void onEnable() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public GuiBuilder create(String title, int size) {
        return new GuiBuilder(messages.color(title), size);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory != null && inventory.getHolder() instanceof YALPGui) {
            ((YALPGui) inventory.getHolder()).handle(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // Inventory holder ownership lets the JVM release GUI references naturally.
    }
}
