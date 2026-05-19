package voiidstudios.yalp.bukkit.compatibility;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import voiidstudios.yalp.bukkit.platform.BukkitPlatformDetector;
import voiidstudios.yalp.core.compatibility.CompatibilityService;
import voiidstudios.yalp.core.component.YALPComponent;
import voiidstudios.yalp.core.platform.PlatformInfo;
import voiidstudios.yalp.core.platform.PlatformType;

import java.util.Locale;

public final class BukkitCompatibilityComponent implements CompatibilityService, YALPComponent {
    private final PlatformInfo platform;

    public BukkitCompatibilityComponent(PlatformInfo platform) {
        this.platform = platform;
    }

    @Override
    public String getId() {
        return "compatibility";
    }

    @Override
    public String getServerSoftware() {
        return platform.getName();
    }

    @Override
    public boolean isBukkit() {
        return platform.getType() == PlatformType.BUKKIT;
    }

    @Override
    public boolean isSpigot() {
        return platform.getType() == PlatformType.SPIGOT || BukkitPlatformDetector.hasClass("org.spigotmc.SpigotConfig");
    }

    @Override
    public boolean isPaper() {
        return platform.getType() == PlatformType.PAPER || isPurpur() || isFolia();
    }

    @Override
    public boolean isFolia() {
        return platform.getType() == PlatformType.FOLIA;
    }

    @Override
    public boolean isPurpur() {
        return platform.getType() == PlatformType.PURPUR || Bukkit.getName().toLowerCase(Locale.ROOT).contains("purpur");
    }

    @Override
    public String getMinecraftVersion() {
        return platform.getMinecraftVersion();
    }

    @Override
    public boolean isLegacy() {
        return platform.isLegacy();
    }

    @Override
    public boolean isModern() {
        return platform.isModern();
    }

    @Override
    public boolean supportsAdventure() {
        return BukkitPlatformDetector.hasClass("net.kyori.adventure.text.Component");
    }

    @Override
    public boolean supportsItemFlags() {
        return BukkitPlatformDetector.hasClass("org.bukkit.inventory.ItemFlag");
    }

    @Override
    public boolean supportsHexColors() {
        return compareMinor(getMinecraftVersion(), 16) >= 0;
    }

    @Override
    public boolean supportsPlayerHeadsModernApi() {
        return Material.matchMaterial("PLAYER_HEAD") != null;
    }

    private int compareMinor(String version, int targetMinor) {
        if (version == null || !version.startsWith("1.")) {
            return -1;
        }
        try {
            int minor = Integer.parseInt(version.substring(2).split("\\.")[0]);
            return Integer.compare(minor, targetMinor);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}
