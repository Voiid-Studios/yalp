package voiidstudios.yalp.core.compatibility;

public interface CompatibilityService {
    String getServerSoftware();

    boolean isBukkit();

    boolean isSpigot();

    boolean isPaper();

    boolean isFolia();

    boolean isPurpur();

    String getMinecraftVersion();

    boolean isLegacy();

    boolean isModern();

    boolean supportsAdventure();

    boolean supportsItemFlags();

    boolean supportsHexColors();

    boolean supportsPlayerHeadsModernApi();
}
