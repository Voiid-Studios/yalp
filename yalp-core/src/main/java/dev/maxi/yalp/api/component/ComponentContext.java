package dev.maxi.yalp.api.component;

import dev.maxi.yalp.api.YALPApi;
import dev.maxi.yalp.api.platform.ServerCompatibility;
import dev.maxi.yalp.api.util.YALPLogger;
import org.bukkit.plugin.java.JavaPlugin;

public final class ComponentContext {
    private final JavaPlugin plugin;
    private final YALPApi api;
    private final YALPLogger logger;
    private final ServerCompatibility compatibility;

    public ComponentContext(JavaPlugin plugin, YALPApi api, YALPLogger logger, ServerCompatibility compatibility) {
        this.plugin = plugin;
        this.api = api;
        this.logger = logger;
        this.compatibility = compatibility;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public YALPApi getApi() {
        return api;
    }

    public YALPLogger getLogger() {
        return logger;
    }

    public ServerCompatibility getCompatibility() {
        return compatibility;
    }
}
