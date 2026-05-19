package voiidstudios.yalp.bukkit.platform;

import org.bukkit.Bukkit;
import voiidstudios.yalp.core.platform.PlatformInfo;
import voiidstudios.yalp.core.platform.PlatformType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BukkitPlatformDetector {
    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\d+\\.\\d+)(?:\\.\\d+)?");

    private BukkitPlatformDetector() {
    }

    public static PlatformInfo detect() {
        String version = minecraftVersion();
        return new PlatformInfo(type(), Bukkit.getName(), version, isLegacy(version));
    }

    public static PlatformType type() {
        if (hasClass("io.papermc.paper.threadedregions.RegionizedServer")) {
            return PlatformType.FOLIA;
        }
        if (hasClass("org.purpurmc.purpur.PurpurConfig")) {
            return PlatformType.PURPUR;
        }
        if (hasClass("com.destroystokyo.paper.PaperConfig") || hasClass("io.papermc.paper.configuration.Configuration")) {
            return PlatformType.PAPER;
        }
        if (hasClass("org.spigotmc.SpigotConfig")) {
            return PlatformType.SPIGOT;
        }
        return PlatformType.BUKKIT;
    }

    public static boolean hasClass(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    private static String minecraftVersion() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        Matcher matcher = VERSION_PATTERN.matcher(bukkitVersion == null ? "" : bukkitVersion);
        return matcher.find() ? matcher.group(1) : "unknown";
    }

    private static boolean isLegacy(String version) {
        return version.startsWith("1.8") || version.startsWith("1.9") || version.startsWith("1.10")
                || version.startsWith("1.11") || version.startsWith("1.12");
    }
}
