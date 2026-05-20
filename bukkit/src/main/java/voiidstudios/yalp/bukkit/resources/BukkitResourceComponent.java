package voiidstudios.yalp.bukkit.resources;

import org.bukkit.plugin.java.JavaPlugin;
import voiidstudios.yalp.core.component.YALPComponent;
import voiidstudios.yalp.core.logging.YALPLogger;
import voiidstudios.yalp.core.resources.ResourceService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class BukkitResourceComponent implements ResourceService, YALPComponent {
    private final YALPLogger logger;

    public BukkitResourceComponent(YALPLogger logger) {
        this.logger = logger;
    }

    @Override
    public String getId() { return "resources"; }

    @Override
    public Optional<String> readText(Object owner, String path) {
        List<String> lines = readLines(owner, path);
        if (lines.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(String.join("\n", lines));
    }

    @Override
    public List<String> readLines(Object owner, String path) {
        JavaPlugin plugin = plugin(owner);
        List<String> lines = new ArrayList<>();
        if (plugin == null) {
            return lines;
        }
        try (InputStream stream = plugin.getResource(path)) {
            if (stream == null) {
                logger.debug("Missing resource: " + path);
                return lines;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
        } catch (IOException exception) {
            logger.exception("Unable to read resource " + path, exception);
        }
        return lines;
    }

    @Override
    public boolean copyResource(Object owner, String resourcePath, File destination, boolean replace) {
        JavaPlugin plugin = plugin(owner);
        if (plugin == null || destination == null || (!replace && destination.exists())) {
            return false;
        }
        if (destination.getParentFile() != null) {
            destination.getParentFile().mkdirs();
        }
        try (InputStream input = plugin.getResource(resourcePath)) {
            if (input == null) {
                logger.debug("Missing resource: " + resourcePath);
                return false;
            }
            try (FileOutputStream output = new FileOutputStream(destination)) {
                byte[] buffer = new byte[4096];
                int read;
                while ((read = input.read(buffer)) >= 0) {
                    output.write(buffer, 0, read);
                }
            }
            return true;
        } catch (IOException exception) {
            logger.exception("Unable to copy resource " + resourcePath, exception);
            return false;
        }
    }

    @Override
    public boolean copyDefault(Object owner, String resourcePath) {
        JavaPlugin plugin = plugin(owner);
        if (plugin == null) {
            return false;
        }
        return copyResource(plugin, resourcePath, new File(plugin.getDataFolder(), resourcePath), false);
    }

    @Override
    public boolean copyDefaultFolder(Object owner, String resourceFolder) {
        logger.debug("Folder resource copying requires explicit file names on Bukkit.");
        return false;
    }

    @Override
    public boolean resourceExists(Object owner, String path) {
        JavaPlugin plugin = plugin(owner);
        if (plugin == null) {
            return false;
        }
        try (InputStream stream = plugin.getResource(path)) {
            return stream != null;
        } catch (IOException ignored) {
            return false;
        }
    }

    private JavaPlugin plugin(Object owner) {
        return owner instanceof JavaPlugin ? (JavaPlugin) owner : null;
    }
}
