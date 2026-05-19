package dev.maxi.yalp.api.platform;

import org.bukkit.Bukkit;

public final class ServerCompatibility {
    private final boolean folia;
    private final boolean paper;
    private final boolean legacy;
    private final String bukkitVersion;

    private ServerCompatibility(boolean folia, boolean paper, boolean legacy, String bukkitVersion) {
        this.folia = folia;
        this.paper = paper;
        this.legacy = legacy;
        this.bukkitVersion = bukkitVersion;
    }

    public static ServerCompatibility detect() {
        String version = Bukkit.getBukkitVersion();
        boolean folia = hasClass("io.papermc.paper.threadedregions.RegionizedServer");
        boolean paper = folia || hasClass("com.destroystokyo.paper.PaperConfig") || hasClass("io.papermc.paper.configuration.Configuration");
        boolean legacy = version.startsWith("1.8") || version.startsWith("1.9") || version.startsWith("1.10")
                || version.startsWith("1.11") || version.startsWith("1.12");
        return new ServerCompatibility(folia, paper, legacy, version);
    }

    private static boolean hasClass(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    public boolean isFolia() {
        return folia;
    }

    public boolean isPaper() {
        return paper;
    }

    public boolean isLegacy() {
        return legacy;
    }

    public String getBukkitVersion() {
        return bukkitVersion;
    }
}
