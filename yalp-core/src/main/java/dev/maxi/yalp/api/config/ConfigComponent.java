package dev.maxi.yalp.api.config;

import dev.maxi.yalp.api.component.ComponentContext;
import dev.maxi.yalp.api.component.YALPComponent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ConfigComponent implements YALPComponent {
    private final Map<String, FileConfiguration> configs = new LinkedHashMap<>();
    private JavaPlugin plugin;

    @Override
    public String getId() {
        return "config";
    }

    @Override
    public void onLoad(ComponentContext context) {
        this.plugin = context.getPlugin();
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
    }

    public FileConfiguration load(String fileName) {
        return load(plugin, fileName, true);
    }

    public FileConfiguration load(String fileName, boolean copyDefaults) {
        return load(plugin, fileName, copyDefaults);
    }

    public FileConfiguration load(JavaPlugin owner, String fileName) {
        return load(owner, fileName, true);
    }

    public FileConfiguration load(JavaPlugin owner, String fileName, boolean copyDefaults) {
        ensureOwner(owner);
        String normalized = normalize(fileName);
        File file = new File(owner.getDataFolder(), normalized);
        if (!owner.getDataFolder().exists()) {
            owner.getDataFolder().mkdirs();
        }
        if (copyDefaults && !file.exists()) {
            try {
                owner.saveResource(normalized, false);
            } catch (IllegalArgumentException ignored) {
                // No bundled default resource; an empty YAML file is fine for utility use.
            }
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        configs.put(key(owner, normalized), config);
        return config;
    }

    public void save(String fileName) {
        ensurePlugin();
        save(plugin, fileName);
    }

    public void save(JavaPlugin owner, String fileName) {
        ensureOwner(owner);
        String normalized = normalize(fileName);
        FileConfiguration config = configs.get(key(owner, normalized));
        if (config == null) {
            throw new IllegalStateException("Config is not loaded: " + normalized);
        }
        try {
            config.save(new File(owner.getDataFolder(), normalized));
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to save config " + normalized, exception);
        }
    }

    public FileConfiguration reload(String fileName) {
        String normalized = normalize(fileName);
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), normalized));
        configs.put(key(plugin, normalized), config);
        return config;
    }

    @Override
    public void reload() {
        for (String fileName : new ArrayList<>(configs.keySet())) {
            // External plugin configs are intentionally left alone on YALP reload.
            if (fileName.startsWith(plugin.getName() + ":")) {
                reload(fileName.substring(plugin.getName().length() + 1));
            }
        }
    }

    private String normalize(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("Config file name cannot be empty.");
        }
        return fileName.endsWith(".yml") ? fileName : fileName + ".yml";
    }

    private void ensurePlugin() {
        if (plugin == null) {
            throw new IllegalStateException("ConfigComponent has not loaded yet.");
        }
    }

    private void ensureOwner(JavaPlugin owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Config owner plugin cannot be null.");
        }
    }

    private String key(JavaPlugin owner, String fileName) {
        return owner.getName() + ":" + fileName;
    }
}
