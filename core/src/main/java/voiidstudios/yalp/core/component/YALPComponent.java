package voiidstudios.yalp.core.component;

import java.util.Collections;
import java.util.List;

public interface YALPComponent {
    String getId();

    default List<String> getDependencies() {
        return Collections.emptyList();
    }

    default void onLoad(ComponentContext context) {
    }

    default void onEnable() {
    }

    default void onDisable() {
    }

    default void reload() {
    }
}
