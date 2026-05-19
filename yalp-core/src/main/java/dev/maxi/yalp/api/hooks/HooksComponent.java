package dev.maxi.yalp.api.hooks;

import dev.maxi.yalp.api.component.YALPComponent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public final class HooksComponent implements YALPComponent {
    @Override
    public String getId() {
        return "hooks";
    }

    public boolean isPluginEnabled(String pluginName) {
        return Bukkit.getPluginManager().isPluginEnabled(pluginName);
    }

    public Optional<Plugin> getPlugin(String pluginName) {
        return Optional.ofNullable(Bukkit.getPluginManager().getPlugin(pluginName));
    }

    public boolean hasPlaceholderAPI() {
        return isPluginEnabled("PlaceholderAPI");
    }

    public boolean hasVault() {
        return isPluginEnabled("Vault");
    }

    public boolean hasLuckPerms() {
        return isPluginEnabled("LuckPerms");
    }

    public boolean hasProtocolLib() {
        return isPluginEnabled("ProtocolLib");
    }

    public boolean hasItemsAdder() {
        return isPluginEnabled("ItemsAdder");
    }

    public boolean hasWorldGuard() {
        return isPluginEnabled("WorldGuard");
    }

    public boolean hasWorldEdit() {
        return isPluginEnabled("WorldEdit");
    }
}
