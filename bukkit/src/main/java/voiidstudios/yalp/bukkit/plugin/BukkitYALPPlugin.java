package voiidstudios.yalp.bukkit.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import voiidstudios.yalp.bukkit.api.BukkitYALPApi;
import voiidstudios.yalp.bukkit.compatibility.BukkitCompatibilityComponent;
import voiidstudios.yalp.bukkit.component.BukkitComponentContext;
import voiidstudios.yalp.bukkit.commands.BukkitCommandComponent;
import voiidstudios.yalp.bukkit.config.BukkitConfigService;
import voiidstudios.yalp.bukkit.cooldown.BukkitCooldownComponent;
import voiidstudios.yalp.bukkit.definitions.BukkitYamlFolderComponent;
import voiidstudios.yalp.bukkit.gui.BukkitGuiComponent;
import voiidstudios.yalp.bukkit.hooks.BukkitHooksComponent;
import voiidstudios.yalp.bukkit.items.BukkitItemBuilder;
import voiidstudios.yalp.bukkit.json.BukkitJsonFileComponent;
import voiidstudios.yalp.bukkit.messages.BukkitMessages;
import voiidstudios.yalp.bukkit.platform.BukkitPlatformAdapter;
import voiidstudios.yalp.bukkit.platform.BukkitPlatformDetector;
import voiidstudios.yalp.bukkit.resources.BukkitResourceComponent;
import voiidstudios.yalp.bukkit.scheduler.BukkitSchedulerService;
import voiidstudios.yalp.bukkit.text.BukkitRichTextComponent;
import voiidstudios.yalp.bukkit.translation.BukkitTranslationComponent;
import voiidstudios.yalp.core.api.YALPApi;
import voiidstudios.yalp.core.api.YALPProvider;
import voiidstudios.yalp.core.component.registry.ComponentManager;
import voiidstudios.yalp.core.logging.ConsoleYALPLogger;
import voiidstudios.yalp.core.logging.YALPLogger;
import voiidstudios.yalp.core.logging.LoggerComponent;
import voiidstudios.yalp.core.platform.PlatformInfo;
import voiidstudios.yalp.core.platform.PlatformLifecycleState;

public final class BukkitYALPPlugin extends JavaPlugin {
    private static boolean enabledBefore;

    private BukkitYALPApi api;
    private ComponentManager components;
    private YALPLogger logger;
    private BukkitPlatformAdapter platformAdapter;

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
        platformAdapter = new BukkitPlatformAdapter(this, platform, logger, scheduler);
        BukkitCooldownComponent cooldowns = new BukkitCooldownComponent();
        BukkitConfigService configs = new BukkitConfigService();
        BukkitHooksComponent hooks = new BukkitHooksComponent();
        BukkitItemBuilder items = new BukkitItemBuilder(messages, logger);
        BukkitGuiComponent guis = new BukkitGuiComponent(this, messages);
        BukkitTranslationComponent translations = new BukkitTranslationComponent(messages, logger);
        BukkitYamlFolderComponent yamlFolders = new BukkitYamlFolderComponent(logger);
        BukkitCommandComponent commands = new BukkitCommandComponent(messages);
        BukkitResourceComponent resources = new BukkitResourceComponent(logger);
        BukkitJsonFileComponent jsonFiles = new BukkitJsonFileComponent(logger);
        BukkitRichTextComponent richText = new BukkitRichTextComponent(messages);

        api = new BukkitYALPApi(getDescription().getVersion(), platform, platformAdapter, components, logger,
                messages, cooldowns, scheduler, configs, hooks, compatibility, items, guis,
                translations, yamlFolders, commands, resources, jsonFiles, richText);

        components.register(new LoggerComponent(logger));
        components.register(messages);
        components.register(compatibility);
        components.register(scheduler);
        components.register(cooldowns);
        components.register(configs);
        components.register(hooks);
        components.register(items);
        components.register(guis);
        components.register(translations);
        components.register(yamlFolders);
        components.register(commands);
        components.register(resources);
        components.register(jsonFiles);
        components.register(richText);
        components.loadAll(new BukkitComponentContext(this, api, logger, platform));
        platformAdapter.setLifecycleState(PlatformLifecycleState.INITIALIZED);
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
        platformAdapter.setLifecycleState(PlatformLifecycleState.ENABLED);
        logger.info("yup");
    }

    @Override
    public void onDisable() {
        if (components != null) {
            platformAdapter.setLifecycleState(PlatformLifecycleState.SHUTTING_DOWN);
            components.disableAll();
        }
        Bukkit.getServicesManager().unregister(YALPApi.class, api);
        Bukkit.getServicesManager().unregister(BukkitYALPApi.class, api);
        YALPProvider.unregister(api);
        if (platformAdapter != null) {
            platformAdapter.setLifecycleState(PlatformLifecycleState.DISABLED);
        }
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
