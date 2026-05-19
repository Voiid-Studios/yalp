package voiidstudios.yalp.bukkit.hooks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import voiidstudios.yalp.core.component.YALPComponent;
import voiidstudios.yalp.core.hooks.HooksService;

import java.util.Optional;

public final class BukkitHooksComponent implements HooksService, YALPComponent {
    @Override
    public String getId() {
        return "hooks";
    }

    @Override
    public boolean isPluginEnabled(String pluginName) {
        return Bukkit.getPluginManager().isPluginEnabled(pluginName);
    }

    @Override
    public Optional<Object> getPlugin(String pluginName) {
        return Optional.ofNullable(Bukkit.getPluginManager().getPlugin(pluginName)).map(plugin -> (Object) plugin);
    }

    public Optional<Plugin> getBukkitPlugin(String pluginName) {
        return Optional.ofNullable(Bukkit.getPluginManager().getPlugin(pluginName));
    }

    @Override
    public boolean hasPlaceholderAPI() {
        return isPluginEnabled("PlaceholderAPI");
    }

    @Override
    public boolean hasVault() {
        return isPluginEnabled("Vault");
    }

    @Override
    public boolean hasLuckPerms() {
        return isPluginEnabled("LuckPerms");
    }

    @Override
    public boolean hasProtocolLib() {
        return isPluginEnabled("ProtocolLib");
    }

    @Override
    public boolean hasItemsAdder() {
        return isPluginEnabled("ItemsAdder");
    }

    @Override
    public boolean hasWorldGuard() {
        return isPluginEnabled("WorldGuard");
    }

    @Override
    public boolean hasWorldEdit() {
        return isPluginEnabled("WorldEdit");
    }
}
