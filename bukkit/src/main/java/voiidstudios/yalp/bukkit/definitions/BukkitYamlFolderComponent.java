package voiidstudios.yalp.bukkit.definitions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import voiidstudios.yalp.core.component.YALPComponent;
import voiidstudios.yalp.core.definitions.YamlDefinition;
import voiidstudios.yalp.core.definitions.YamlDefinitionRegistry;
import voiidstudios.yalp.core.definitions.YamlFolderService;
import voiidstudios.yalp.core.logging.YALPLogger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class BukkitYamlFolderComponent implements YamlFolderService, YALPComponent {
    private final YALPLogger logger;

    public BukkitYamlFolderComponent(YALPLogger logger) {
        this.logger = logger;
    }

    @Override
    public String getId() { return "yaml-folders"; }

    @Override
    public YamlDefinitionRegistry load(Object owner, String folderName, String rootSection) {
        if (!(owner instanceof JavaPlugin)) {
            throw new IllegalArgumentException("Bukkit YAML folder registries require a JavaPlugin owner.");
        }
        BukkitYamlDefinitionRegistry registry = new BukkitYamlDefinitionRegistry((JavaPlugin) owner, folderName, rootSection, logger);
        registry.reload();
        return registry;
    }

    private static final class BukkitYamlDefinitionRegistry implements YamlDefinitionRegistry {
        private final JavaPlugin plugin;
        private final String folderName;
        private final String rootSection;
        private final YALPLogger logger;
        private final Map<String, YamlDefinition> definitions = new LinkedHashMap<>();
        private final Map<String, File> files = new LinkedHashMap<>();
        private final Map<String, FileConfiguration> configs = new LinkedHashMap<>();

        private BukkitYamlDefinitionRegistry(JavaPlugin plugin, String folderName, String rootSection, YALPLogger logger) {
            this.plugin = plugin;
            this.folderName = folderName;
            this.rootSection = rootSection;
            this.logger = logger;
        }

        @Override
        public Optional<YamlDefinition> get(String id) { return Optional.ofNullable(definitions.get(id)); }

        @Override
        public List<String> getIds() { return new ArrayList<>(definitions.keySet()); }

        @Override
        public List<YamlDefinition> getAll() { return new ArrayList<>(definitions.values()); }

        @Override
        public List<YamlDefinition> getEnabled() {
            List<YamlDefinition> enabled = new ArrayList<>();
            for (YamlDefinition definition : definitions.values()) {
                if (definition.isEnabled()) enabled.add(definition);
            }
            return enabled;
        }

        @Override
        public boolean setEnabled(String id, boolean enabled) {
            YamlDefinition definition = definitions.get(id);
            if (definition == null) return false;
            definition.setEnabled(enabled);
            return true;
        }

        @Override
        public boolean save(String id) {
            YamlDefinition definition = definitions.get(id);
            File file = files.get(id);
            FileConfiguration config = configs.get(id);
            if (definition == null || file == null || config == null) return false;
            config.set(path(id, "enabled"), definition.isEnabled());
            try {
                config.save(file);
                return true;
            } catch (IOException exception) {
                logger.exception("Unable to save YAML definition " + id, exception);
                return false;
            }
        }

        @Override
        public void reload() {
            definitions.clear();
            files.clear();
            configs.clear();
            File folder = new File(plugin.getDataFolder(), folderName);
            folder.mkdirs();
            loadFolder(folder);
        }

        private void loadFolder(File folder) {
            File[] listed = folder.listFiles();
            if (listed == null) return;
            for (File file : listed) {
                if (file.isDirectory()) {
                    loadFolder(file);
                    continue;
                }
                if (!file.getName().endsWith(".yml")) continue;
                loadFile(file);
            }
        }

        private void loadFile(File file) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            ConfigurationSection root = config.getConfigurationSection(rootSection);
            if (root == null) {
                logger.warning("Invalid YAML definition file " + file.getName() + ": missing section " + rootSection);
                return;
            }
            for (String id : root.getKeys(false)) {
                String base = rootSection + "." + id;
                ConfigurationSection section = config.getConfigurationSection(base);
                if (section == null) continue;
                Map<String, Object> options = new LinkedHashMap<>();
                ConfigurationSection optionSection = section.getConfigurationSection("options");
                if (optionSection != null) {
                    for (String key : optionSection.getKeys(false)) {
                        options.put(key, optionSection.get(key));
                    }
                }
                YamlDefinition definition = new YamlDefinition(id, section.getBoolean("enabled", true),
                        section.getString("name", id), section.getString("description", ""),
                        section.getString("type", ""), options, file);
                definitions.put(id, definition);
                files.put(id, file);
                configs.put(id, config);
            }
        }

        private String path(String id, String child) { return rootSection + "." + id + "." + child; }
    }
}
