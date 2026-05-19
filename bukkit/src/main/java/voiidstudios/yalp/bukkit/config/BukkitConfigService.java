package voiidstudios.yalp.bukkit.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import voiidstudios.yalp.bukkit.component.BukkitComponentContext;
import voiidstudios.yalp.core.component.ComponentContext;
import voiidstudios.yalp.core.component.YALPComponent;
import voiidstudios.yalp.core.config.ConfigService;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class BukkitConfigService implements ConfigService, YALPComponent {
    private final Map<String, FileConfiguration> configs = new LinkedHashMap<>();
    private JavaPlugin plugin;

    @Override
    public String getId() {
        return "config";
    }

    @Override
    public void onLoad(ComponentContext context) {
        this.plugin = ((BukkitComponentContext) context).getPlugin();
    }

    public FileConfiguration load(JavaPlugin owner, String fileName) {
        return load(owner, fileName, true);
    }

    public FileConfiguration load(JavaPlugin owner, String fileName, boolean copyDefaults) {
        String normalized = normalize(fileName);
        if (!owner.getDataFolder().exists()) {
            owner.getDataFolder().mkdirs();
        }
        File file = new File(owner.getDataFolder(), normalized);
        if (copyDefaults && !file.exists()) {
            try {
                owner.saveResource(normalized, false);
            } catch (IllegalArgumentException ignored) {
            }
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        configs.put(key(owner.getName(), normalized), config);
        return config;
    }

    public void save(JavaPlugin owner, String fileName) {
        String normalized = normalize(fileName);
        FileConfiguration config = configs.get(key(owner.getName(), normalized));
        if (config == null) {
            throw new IllegalStateException("Config is not loaded: " + normalized);
        }
        try {
            config.save(new File(owner.getDataFolder(), normalized));
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to save config " + normalized, exception);
        }
    }

    @Override
    public Optional<Object> load(String owner, String fileName) {
        if (plugin == null || !plugin.getName().equals(owner)) {
            return Optional.empty();
        }
        return Optional.of(load(plugin, fileName));
    }

    @Override
    public void save(String owner, String fileName) {
        if (plugin != null && plugin.getName().equals(owner)) {
            save(plugin, fileName);
        }
    }

    @Override
    public Optional<Object> reload(String owner, String fileName) {
        if (plugin == null || !plugin.getName().equals(owner)) {
            return Optional.empty();
        }
        String normalized = normalize(fileName);
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), normalized));
        configs.put(key(owner, normalized), config);
        return Optional.of(config);
    }

    private String normalize(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("Config file name cannot be empty.");
        }
        return fileName.endsWith(".yml") ? fileName : fileName + ".yml";
    }

    private String key(String owner, String fileName) {
        return owner + ":" + fileName;
    }
}
