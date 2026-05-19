package voiidstudios.yalp.core.component.registry;

import voiidstudios.yalp.core.component.YALPComponent;

import java.util.Collection;
import java.util.Optional;

public interface ComponentRegistry {
    void register(YALPComponent component);

    <T extends YALPComponent> Optional<T> get(Class<T> componentType);

    Optional<YALPComponent> get(String id);

    boolean contains(String id);

    Collection<YALPComponent> all();
}
