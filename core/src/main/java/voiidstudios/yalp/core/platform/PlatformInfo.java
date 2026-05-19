package voiidstudios.yalp.core.platform;

public final class PlatformInfo {
    private final PlatformType type;
    private final String name;
    private final String minecraftVersion;
    private final boolean legacy;

    public PlatformInfo(PlatformType type, String name, String minecraftVersion, boolean legacy) {
        this.type = type == null ? PlatformType.UNKNOWN : type;
        this.name = name == null ? "unknown" : name;
        this.minecraftVersion = minecraftVersion == null ? "unknown" : minecraftVersion;
        this.legacy = legacy;
    }

    public PlatformType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    public boolean isLegacy() {
        return legacy;
    }

    public boolean isModern() {
        return !legacy;
    }
}
