package voiidstudios.yalp.core;

import voiidstudios.yalp.core.api.YALPApi;
import voiidstudios.yalp.core.compatibility.CompatibilityService;
import voiidstudios.yalp.core.component.YALPComponent;
import voiidstudios.yalp.core.component.registry.ComponentManager;
import voiidstudios.yalp.core.component.registry.ComponentRegistry;
import voiidstudios.yalp.core.config.ConfigService;
import voiidstudios.yalp.core.cooldown.CooldownService;
import voiidstudios.yalp.core.commands.CommandService;
import voiidstudios.yalp.core.definitions.YamlFolderService;
import voiidstudios.yalp.core.hooks.HooksService;
import voiidstudios.yalp.core.json.JsonFileService;
import voiidstudios.yalp.core.logging.YALPLogger;
import voiidstudios.yalp.core.messages.MessageFormatter;
import voiidstudios.yalp.core.platform.PlatformAdapter;
import voiidstudios.yalp.core.platform.PlatformInfo;
import voiidstudios.yalp.core.resources.ResourceService;
import voiidstudios.yalp.core.scheduler.SchedulerService;
import voiidstudios.yalp.core.text.RichTextService;
import voiidstudios.yalp.core.translation.TranslationService;

import java.util.Optional;

public class DefaultYALPApi implements YALPApi {
    private final String version;
    private final PlatformInfo platform;
    private final PlatformAdapter platformAdapter;
    private final ComponentManager components;
    private final YALPLogger logger;
    private final MessageFormatter messages;
    private final CooldownService cooldowns;
    private final SchedulerService scheduler;
    private final ConfigService configs;
    private final HooksService hooks;
    private final CompatibilityService compatibility;
    private final TranslationService translations;
    private final YamlFolderService yamlFolders;
    private final CommandService commands;
    private final ResourceService resources;
    private final JsonFileService jsonFiles;
    private final RichTextService richText;

    public DefaultYALPApi(String version, PlatformInfo platform, PlatformAdapter platformAdapter, ComponentManager components, YALPLogger logger,
                          MessageFormatter messages, CooldownService cooldowns, SchedulerService scheduler,
                          ConfigService configs, HooksService hooks, CompatibilityService compatibility,
                          TranslationService translations, YamlFolderService yamlFolders, CommandService commands,
                          ResourceService resources, JsonFileService jsonFiles, RichTextService richText) {
        this.version = version;
        this.platform = platform;
        this.platformAdapter = platformAdapter;
        this.components = components;
        this.logger = logger;
        this.messages = messages;
        this.cooldowns = cooldowns;
        this.scheduler = scheduler;
        this.configs = configs;
        this.hooks = hooks;
        this.compatibility = compatibility;
        this.translations = translations;
        this.yamlFolders = yamlFolders;
        this.commands = commands;
        this.resources = resources;
        this.jsonFiles = jsonFiles;
        this.richText = richText;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public PlatformInfo platform() {
        return platform;
    }

    @Override
    public PlatformAdapter platformAdapter() {
        return platformAdapter;
    }

    @Override
    public ComponentRegistry getComponentRegistry() {
        return components;
    }

    @Override
    public <T extends YALPComponent> Optional<T> getComponent(Class<T> componentType) {
        return components.get(componentType);
    }

    @Override
    public Optional<YALPComponent> getComponent(String id) {
        return components.get(id);
    }

    @Override
    public YALPLogger logger() {
        return logger;
    }

    @Override
    public MessageFormatter messages() {
        return messages;
    }

    @Override
    public CooldownService cooldowns() {
        return cooldowns;
    }

    @Override
    public SchedulerService scheduler() {
        return scheduler;
    }

    @Override
    public ConfigService configs() {
        return configs;
    }

    @Override
    public HooksService hooks() {
        return hooks;
    }

    @Override
    public CompatibilityService compatibility() {
        return compatibility;
    }

    @Override
    public TranslationService translations() {
        return translations;
    }

    @Override
    public YamlFolderService yamlFolders() {
        return yamlFolders;
    }

    @Override
    public CommandService commands() {
        return commands;
    }

    @Override
    public ResourceService resources() {
        return resources;
    }

    @Override
    public JsonFileService jsonFiles() {
        return jsonFiles;
    }

    @Override
    public RichTextService richText() {
        return richText;
    }
}
