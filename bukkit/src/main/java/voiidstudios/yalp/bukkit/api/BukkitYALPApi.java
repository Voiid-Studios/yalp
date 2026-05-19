package voiidstudios.yalp.bukkit.api;

import voiidstudios.yalp.bukkit.config.BukkitConfigService;
import voiidstudios.yalp.bukkit.gui.BukkitGuiComponent;
import voiidstudios.yalp.bukkit.items.BukkitItemBuilder;
import voiidstudios.yalp.bukkit.messages.BukkitMessages;
import voiidstudios.yalp.bukkit.scheduler.BukkitSchedulerService;
import voiidstudios.yalp.core.DefaultYALPApi;
import voiidstudios.yalp.core.compatibility.CompatibilityService;
import voiidstudios.yalp.core.component.registry.ComponentManager;
import voiidstudios.yalp.core.cooldown.CooldownService;
import voiidstudios.yalp.core.hooks.HooksService;
import voiidstudios.yalp.core.logging.YALPLogger;
import voiidstudios.yalp.core.platform.PlatformInfo;

public final class BukkitYALPApi extends DefaultYALPApi {
    private final BukkitMessages messages;
    private final BukkitSchedulerService scheduler;
    private final BukkitConfigService configs;
    private final BukkitItemBuilder items;
    private final BukkitGuiComponent guis;

    public BukkitYALPApi(String version, PlatformInfo platform, ComponentManager components, YALPLogger logger,
                         BukkitMessages messages, CooldownService cooldowns, BukkitSchedulerService scheduler,
                         BukkitConfigService configs, HooksService hooks, CompatibilityService compatibility,
                         BukkitItemBuilder items, BukkitGuiComponent guis) {
        super(version, platform, components, logger, messages, cooldowns, scheduler, configs, hooks, compatibility);
        this.messages = messages;
        this.scheduler = scheduler;
        this.configs = configs;
        this.items = items;
        this.guis = guis;
    }

    @Override
    public BukkitMessages messages() {
        return messages;
    }

    @Override
    public BukkitSchedulerService scheduler() {
        return scheduler;
    }

    @Override
    public BukkitConfigService configs() {
        return configs;
    }

    public BukkitItemBuilder items() {
        return items;
    }

    public BukkitGuiComponent guis() {
        return guis;
    }
}
