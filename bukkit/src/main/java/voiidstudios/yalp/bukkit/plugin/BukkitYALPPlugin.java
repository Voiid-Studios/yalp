package voiidstudios.yalp.bukkit.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import voiidstudios.yalp.bukkit.api.BukkitYALPApi;
import voiidstudios.yalp.bukkit.compatibility.BukkitCompatibilityComponent;
import voiidstudios.yalp.bukkit.component.BukkitComponentContext;
import voiidstudios.yalp.bukkit.config.BukkitConfigService;
import voiidstudios.yalp.bukkit.cooldown.BukkitCooldownComponent;
import voiidstudios.yalp.bukkit.gui.BukkitGuiComponent;
import voiidstudios.yalp.bukkit.hooks.BukkitHooksComponent;
import voiidstudios.yalp.bukkit.items.BukkitItemBuilder;
import voiidstudios.yalp.bukkit.messages.BukkitMessages;
import voiidstudios.yalp.bukkit.platform.BukkitPlatformDetector;
import voiidstudios.yalp.bukkit.scheduler.BukkitSchedulerService;
import voiidstudios.yalp.core.api.YALPApi;
import voiidstudios.yalp.core.api.YALPProvider;
import voiidstudios.yalp.core.component.registry.ComponentManager;
import voiidstudios.yalp.core.logging.ConsoleYALPLogger;
import voiidstudios.yalp.core.logging.YALPLogger;
import voiidstudios.yalp.core.platform.PlatformInfo;

public final class BukkitYALPPlugin extends JavaPlugin {
    private static boolean enabledBefore;

    private BukkitYALPApi api;
    private ComponentManager components;
    private YALPLogger logger;

    @Override
    public void onLoad() {
        saveDefaultConfig();

        logger = new ConsoleYALPLogger(getLogger());
        logger.setDebugEnabled(getConfig().getBoolean("debug", false));

        PlatformInfo platform = BukkitPlatformDetector.detect();
        components = new ComponentManager(logger);

        BukkitMessages messages = new BukkitMessages();
        BukkitCompatibilityComponent compatibility = new BukkitCompatibilityComponent(platform);
        BukkitSchedulerService scheduler = new BukkitSchedulerService(this, compatibility, logger);
        BukkitCooldownComponent cooldowns = new BukkitCooldownComponent();
        BukkitConfigService configs = new BukkitConfigService();
        BukkitHooksComponent hooks = new BukkitHooksComponent();
        BukkitItemBuilder items = new BukkitItemBuilder(messages, logger);
        BukkitGuiComponent guis = new BukkitGuiComponent(this, messages);

        api = new BukkitYALPApi(getDescription().getVersion(), platform, components, logger,
                messages, cooldowns, scheduler, configs, hooks, compatibility, items, guis);

        components.register(messages);
        components.register(compatibility);
        components.register(scheduler);
        components.register(cooldowns);
        components.register(configs);
        components.register(hooks);
        components.register(items);
        components.register(guis);
        components.loadAll(new BukkitComponentContext(this, api, logger, platform));
    }

    @Override
    public void onEnable() {
        if (enabledBefore) {
            printReloadWarning();
            logger.info("another reload \u00af\\_(\u30c4)_/\u00af");
        }
        enabledBefore = true;

        YALPProvider.register(api, true);
        Bukkit.getServicesManager().register(YALPApi.class, api, this, ServicePriority.Normal);
        Bukkit.getServicesManager().register(BukkitYALPApi.class, api, this, ServicePriority.Normal);
        components.enableAll();
        logger.info("yup");
    }

    @Override
    public void onDisable() {
        if (components != null) {
            components.disableAll();
        }
        Bukkit.getServicesManager().unregister(YALPApi.class, api);
        Bukkit.getServicesManager().unregister(BukkitYALPApi.class, api);
        YALPProvider.unregister(api);
    }

    private void printReloadWarning() {
        getLogger().warning("*****************************************************************");
        getLogger().warning("*** WARNING                                                   ***");
        getLogger().warning("*** RELOADING THE SERVER WHILE YALP IS ENABLED MIGHT          ***");
        getLogger().warning("*** LEAD TO UNEXPECTED ERRORS.                                ***");
        getLogger().warning("***                                                           ***");
        getLogger().warning("*** Please restart the server cleanly before blaming YALP.     ***");
        getLogger().warning("*****************************************************************");
    }
}
