package voiidstudios.yalp.bukkit.translation;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import voiidstudios.yalp.bukkit.messages.BukkitMessages;
import voiidstudios.yalp.core.component.YALPComponent;
import voiidstudios.yalp.core.logging.YALPLogger;
import voiidstudios.yalp.core.translation.TranslationContext;
import voiidstudios.yalp.core.translation.TranslationService;

import java.io.File;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class BukkitTranslationComponent implements TranslationService, YALPComponent {
    private final BukkitMessages messages;
    private final YALPLogger logger;
    private TranslationContext defaultContext;

    public BukkitTranslationComponent(BukkitMessages messages, YALPLogger logger) {
        this.messages = messages;
        this.logger = logger;
    }

    @Override
    public String getId() { return "translations"; }

    public TranslationContext createContext(JavaPlugin plugin, String resourceFolder, String baseLanguage, String selectedLanguage) {
        TranslationContext context = createContext(plugin.getName(), plugin.getDataFolder(), plugin.getClass().getClassLoader(),
                resourceFolder, baseLanguage, selectedLanguage);
        if (defaultContext == null && "YALP".equalsIgnoreCase(plugin.getName())) {
            defaultContext = context;
        }
        return context;
    }

    @Override
    public TranslationContext createContext(String pluginName, File dataFolder, ClassLoader classLoader, String resourceFolder, String baseLanguage, String selectedLanguage) {
        return new BukkitTranslationContext(pluginName, dataFolder, classLoader, resourceFolder, baseLanguage, selectedLanguage, messages, logger);
    }

    @Override
    public String get(String key) { return defaultContext == null ? missing(key) : defaultContext.get(key); }

    @Override
    public String get(String key, Map<String, String> placeholders) { return defaultContext == null ? missing(key) : defaultContext.get(key, placeholders); }

    @Override
    public String get(String key, String... placeholders) { return defaultContext == null ? missing(key) : defaultContext.get(key, placeholders); }

    @Override
    public List<String> getList(String key) { return defaultContext == null ? Collections.singletonList(missing(key)) : defaultContext.getList(key); }

    public void send(CommandSender sender, String key) { messages.send(sender, get(key)); }

    public void send(CommandSender sender, String key, Map<String, String> placeholders) { messages.send(sender, get(key, placeholders)); }

    @Override
    public void reload() {
        if (defaultContext != null) {
            defaultContext.reload();
        }
    }

    private String missing(String key) { return "<missing translation: " + key + ">"; }

    private static final class BukkitTranslationContext implements TranslationContext {
        private final String name;
        private final File dataFolder;
        private final ClassLoader classLoader;
        private final String resourceFolder;
        private final String baseLanguage;
        private final String selectedLanguage;
        private final BukkitMessages messages;
        private final YALPLogger logger;
        private FileConfiguration base;
        private FileConfiguration selected;

        private BukkitTranslationContext(String name, File dataFolder, ClassLoader classLoader, String resourceFolder,
                                         String baseLanguage, String selectedLanguage, BukkitMessages messages, YALPLogger logger) {
            this.name = name;
            this.dataFolder = dataFolder;
            this.classLoader = classLoader;
            this.resourceFolder = resourceFolder;
            this.baseLanguage = baseLanguage == null ? "en_US" : baseLanguage;
            this.selectedLanguage = selectedLanguage == null ? this.baseLanguage : selectedLanguage;
            this.messages = messages;
            this.logger = logger;
            reload();
        }

        @Override
        public String getName() { return name; }

        @Override
        public String getLanguage() { return selectedLanguage; }

        @Override
        public String get(String key) { return get(key, Collections.emptyMap()); }

        @Override
        public String get(String key, Map<String, String> placeholders) {
            String value = selected.getString(key, base.getString(key));
            if (value == null) {
                logger.debug("Missing translation: " + key);
                value = "<missing translation: " + key + ">";
            }
            return messages.format(value, placeholders);
        }

        @Override
        public String get(String key, String... placeholders) { return messages.format(get(key), placeholders); }

        @Override
        public List<String> getList(String key) { return getList(key, Collections.emptyMap()); }

        @Override
        public List<String> getList(String key, Map<String, String> placeholders) {
            List<String> list = selected.getStringList(key);
            if (list.isEmpty()) {
                list = base.getStringList(key);
            }
            if (list.isEmpty()) {
                return Collections.singletonList(get(key, placeholders));
            }
            java.util.List<String> formatted = new java.util.ArrayList<>();
            for (String line : list) {
                formatted.add(messages.format(line, placeholders));
            }
            return formatted;
        }

        public void send(CommandSender sender, String key) { messages.send(sender, get(key)); }

        @Override
        public void reload() {
            File folder = new File(dataFolder, resourceFolder);
            folder.mkdirs();
            base = YamlConfiguration.loadConfiguration(new File(folder, baseLanguage + ".yml"));
            selected = YamlConfiguration.loadConfiguration(new File(folder, selectedLanguage + ".yml"));
        }
    }
}
