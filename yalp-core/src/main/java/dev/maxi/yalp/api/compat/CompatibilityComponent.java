package dev.maxi.yalp.api.compat;

import dev.maxi.yalp.api.component.ComponentContext;
import dev.maxi.yalp.api.component.YALPComponent;
import dev.maxi.yalp.api.platform.ServerCompatibility;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CompatibilityComponent implements YALPComponent {
    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\d+\\.\\d+)(?:\\.\\d+)?");

    private ServerCompatibility compatibility;
    private String serverName;
    private String version;

    @Override
    public String getId() {
        return "compatibility";
    }

    @Override
    public void onLoad(ComponentContext context) {
        compatibility = context.getCompatibility();
        serverName = Bukkit.getName() == null ? "Bukkit" : Bukkit.getName();
        version = detectMinecraftVersion();
    }

    public String getServerSoftware() {
        return serverName;
    }

    public boolean isBukkit() {
        return !isSpigot() && !isPaper() && !isFolia() && !isPurpur();
    }

    public boolean isSpigot() {
        return hasClass("org.spigotmc.SpigotConfig") || lowerServerName().contains("spigot");
    }

    public boolean isPaper() {
        return compatibility != null && compatibility.isPaper();
    }

    public boolean isFolia() {
        return compatibility != null && compatibility.isFolia();
    }

    public boolean isPurpur() {
        return hasClass("org.purpurmc.purpur.PurpurConfig") || lowerServerName().contains("purpur");
    }

    public String getMinecraftVersion() {
        return version;
    }

    public boolean isLegacy() {
        return compatibility != null && compatibility.isLegacy();
    }

    public boolean isModern() {
        return !isLegacy();
    }

    public boolean supportsAdventure() {
        return hasClass("net.kyori.adventure.text.Component");
    }

    public boolean supportsItemFlags() {
        return hasClass("org.bukkit.inventory.ItemFlag");
    }

    public boolean supportsHexColors() {
        return compareMinor(version, 16) >= 0 || hasClass("net.md_5.bungee.api.ChatColor");
    }

    public boolean supportsPlayerHeadsModernApi() {
        return Material.matchMaterial("PLAYER_HEAD") != null;
    }

    private String detectMinecraftVersion() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        Matcher matcher = VERSION_PATTERN.matcher(bukkitVersion == null ? "" : bukkitVersion);
        return matcher.find() ? matcher.group(1) : "unknown";
    }

    private int compareMinor(String detected, int targetMinor) {
        if (detected == null || !detected.startsWith("1.")) {
            return -1;
        }
        try {
            int minor = Integer.parseInt(detected.substring(2).split("\\.")[0]);
            return Integer.compare(minor, targetMinor);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private String lowerServerName() {
        return serverName == null ? "" : serverName.toLowerCase(Locale.ROOT);
    }

    private boolean hasClass(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }
}
