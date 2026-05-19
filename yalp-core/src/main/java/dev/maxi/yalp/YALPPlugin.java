package dev.maxi.yalp;

import dev.maxi.yalp.api.YALPProvider;
import dev.maxi.yalp.api.component.ComponentContext;
import dev.maxi.yalp.api.compat.CompatibilityComponent;
import dev.maxi.yalp.api.config.ConfigComponent;
import dev.maxi.yalp.api.cooldown.CooldownComponent;
import dev.maxi.yalp.api.gui.GuiComponent;
import dev.maxi.yalp.api.hooks.HooksComponent;
import dev.maxi.yalp.api.item.ItemBuilderComponent;
import dev.maxi.yalp.api.logger.LoggerComponent;
import dev.maxi.yalp.api.message.MessagesComponent;
import dev.maxi.yalp.api.platform.ServerCompatibility;
import dev.maxi.yalp.api.scheduler.SchedulerComponent;
import dev.maxi.yalp.api.util.YALPLogger;
import dev.maxi.yalp.internal.ComponentManager;
import dev.maxi.yalp.internal.YALPApiImpl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class YALPPlugin extends JavaPlugin {
    private static boolean enabledBefore;

    private YALPApiImpl api;
    private ComponentManager components;
    private YALPLogger yalpLogger;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        yalpLogger = new YALPLogger(getLogger());
        ServerCompatibility compatibility = ServerCompatibility.detect();
        components = new ComponentManager(yalpLogger);
        api = new YALPApiImpl(getDescription().getVersion(), components, yalpLogger);
        YALPProvider.set(api);

        components.register(new LoggerComponent(yalpLogger));
        components.register(new CompatibilityComponent());
        components.register(new MessagesComponent());
        components.register(new ConfigComponent());
        components.register(new SchedulerComponent());
        components.register(new CooldownComponent());
        components.register(new ItemBuilderComponent());
        components.register(new HooksComponent());
        components.register(new GuiComponent());
        components.loadAll(new ComponentContext(this, api, yalpLogger, compatibility));
    }

    @Override
    public void onEnable() {
        if (enabledBefore) {
            printReloadWarning();
            yalpLogger.info("another reload ¯\\_(ツ)_/¯");
        }
        enabledBefore = true;

        Bukkit.getServicesManager().register(dev.maxi.yalp.api.YALPApi.class, api, this, ServicePriority.Normal);
        components.enableAll();
        yalpLogger.info("yup");
    }

    @Override
    public void onDisable() {
        if (components != null) {
            components.disableAll();
        }
        Bukkit.getServicesManager().unregister(dev.maxi.yalp.api.YALPApi.class, api);
        YALPProvider.unset(api);
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
