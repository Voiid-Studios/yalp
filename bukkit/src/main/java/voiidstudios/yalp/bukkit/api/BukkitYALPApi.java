package voiidstudios.yalp.bukkit.api;

import voiidstudios.yalp.bukkit.config.BukkitConfigService;
import voiidstudios.yalp.bukkit.commands.BukkitCommandComponent;
import voiidstudios.yalp.bukkit.definitions.BukkitYamlFolderComponent;
import voiidstudios.yalp.bukkit.gui.BukkitGuiComponent;
import voiidstudios.yalp.bukkit.items.BukkitItemBuilder;
import voiidstudios.yalp.bukkit.json.BukkitJsonFileComponent;
import voiidstudios.yalp.bukkit.messages.BukkitMessages;
import voiidstudios.yalp.bukkit.platform.BukkitPlatformAdapter;
import voiidstudios.yalp.bukkit.resources.BukkitResourceComponent;
import voiidstudios.yalp.bukkit.scheduler.BukkitSchedulerService;
import voiidstudios.yalp.bukkit.text.BukkitRichTextComponent;
import voiidstudios.yalp.bukkit.translation.BukkitTranslationComponent;
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
    private final BukkitTranslationComponent translations;
    private final BukkitYamlFolderComponent yamlFolders;
    private final BukkitCommandComponent commands;
    private final BukkitResourceComponent resources;
    private final BukkitJsonFileComponent jsonFiles;
    private final BukkitRichTextComponent richText;

    public BukkitYALPApi(String version, PlatformInfo platform, BukkitPlatformAdapter platformAdapter, ComponentManager components, YALPLogger logger,
                         BukkitMessages messages, CooldownService cooldowns, BukkitSchedulerService scheduler,
                         BukkitConfigService configs, HooksService hooks, CompatibilityService compatibility,
                         BukkitItemBuilder items, BukkitGuiComponent guis, BukkitTranslationComponent translations,
                         BukkitYamlFolderComponent yamlFolders, BukkitCommandComponent commands,
                         BukkitResourceComponent resources, BukkitJsonFileComponent jsonFiles,
                         BukkitRichTextComponent richText) {
        super(version, platform, platformAdapter, components, logger, messages, cooldowns, scheduler, configs, hooks, compatibility,
                translations, yamlFolders, commands, resources, jsonFiles, richText);
        this.messages = messages;
        this.scheduler = scheduler;
        this.configs = configs;
        this.items = items;
        this.guis = guis;
        this.translations = translations;
        this.yamlFolders = yamlFolders;
        this.commands = commands;
        this.resources = resources;
        this.jsonFiles = jsonFiles;
        this.richText = richText;
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

    @Override
    public BukkitTranslationComponent translations() { return translations; }

    @Override
    public BukkitYamlFolderComponent yamlFolders() { return yamlFolders; }

    @Override
    public BukkitCommandComponent commands() { return commands; }

    @Override
    public BukkitResourceComponent resources() { return resources; }

    @Override
    public BukkitJsonFileComponent jsonFiles() { return jsonFiles; }

    @Override
    public BukkitRichTextComponent richText() { return richText; }
}
