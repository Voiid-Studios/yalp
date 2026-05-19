package voiidstudios.yalp.core.hooks;

import java.util.Optional;

public interface HooksService {
    boolean isPluginEnabled(String pluginName);

    Optional<Object> getPlugin(String pluginName);

    boolean hasPlaceholderAPI();

    boolean hasVault();

    boolean hasLuckPerms();

    boolean hasProtocolLib();

    boolean hasItemsAdder();

    boolean hasWorldGuard();

    boolean hasWorldEdit();
}
