package voiidstudios.yalp.bukkit.component;

import org.bukkit.plugin.java.JavaPlugin;
import voiidstudios.yalp.core.api.YALPApi;
import voiidstudios.yalp.core.component.ComponentContext;
import voiidstudios.yalp.core.logging.YALPLogger;
import voiidstudios.yalp.core.platform.PlatformInfo;

public final class BukkitComponentContext extends ComponentContext {
    private final JavaPlugin plugin;

    public BukkitComponentContext(JavaPlugin plugin, YALPApi api, YALPLogger logger, PlatformInfo platform) {
        super(api, logger, platform);
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
