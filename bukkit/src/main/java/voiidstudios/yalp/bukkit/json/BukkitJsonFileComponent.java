package voiidstudios.yalp.bukkit.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import voiidstudios.yalp.core.component.YALPComponent;
import voiidstudios.yalp.core.json.JsonFile;
import voiidstudios.yalp.core.json.JsonFileService;
import voiidstudios.yalp.core.logging.YALPLogger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class BukkitJsonFileComponent implements JsonFileService, YALPComponent {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, JsonFile<?>> files = new LinkedHashMap<>();
    private final YALPLogger logger;

    public BukkitJsonFileComponent(YALPLogger logger) {
        this.logger = logger;
    }

    @Override
    public String getId() { return "json-files"; }

    @Override
    public <T> JsonFile<T> register(Object owner, String id, File file, Class<T> type, Supplier<T> defaults) {
        JsonFile<T> jsonFile = new BukkitJsonFile<>(id, file, type, defaults, gson, logger);
        files.put(id, jsonFile);
        return jsonFile;
    }

    @Override
    public void saveAll() {
        for (JsonFile<?> file : files.values()) file.save();
    }

    @Override
    public void reloadAll() {
        for (JsonFile<?> file : files.values()) file.reload();
    }

    private static final class BukkitJsonFile<T> implements JsonFile<T> {
        private final String id;
        private final File file;
        private final Class<T> type;
        private final Supplier<T> defaults;
        private final Gson gson;
        private final YALPLogger logger;
        private T value;

        private BukkitJsonFile(String id, File file, Class<T> type, Supplier<T> defaults, Gson gson, YALPLogger logger) {
            this.id = id; this.file = file; this.type = type; this.defaults = defaults; this.gson = gson; this.logger = logger;
            this.value = defaults.get();
        }

        @Override public String getId() { return id; }
        @Override public T get() { return value; }

        @Override
        public boolean load() {
            if (!file.exists()) {
                value = defaults.get();
                return save();
            }
            try (FileReader reader = new FileReader(file)) {
                T loaded = gson.fromJson(reader, type);
                value = loaded == null ? defaults.get() : loaded;
                return true;
            } catch (Exception exception) {
                File backup = new File(file.getParentFile(), file.getName() + ".broken");
                file.renameTo(backup);
                logger.exception("Invalid JSON file " + file.getName() + "; moved to " + backup.getName(), exception);
                value = defaults.get();
                return save();
            }
        }

        @Override
        public boolean save() {
            if (file.getParentFile() != null) file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(value, writer);
                return true;
            } catch (IOException exception) {
                logger.exception("Unable to save JSON file " + file.getName(), exception);
                return false;
            }
        }

        @Override public boolean reload() { return load(); }
        @Override public boolean resetFromDefault() { value = defaults.get(); return save(); }
    }
}
