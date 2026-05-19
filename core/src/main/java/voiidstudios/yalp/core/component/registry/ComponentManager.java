package voiidstudios.yalp.core.component.registry;

import voiidstudios.yalp.core.component.ComponentContext;
import voiidstudios.yalp.core.component.YALPComponent;
import voiidstudios.yalp.core.component.lifecycle.ComponentLifecycle;
import voiidstudios.yalp.core.logging.YALPLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class ComponentManager implements ComponentRegistry {
    private final Map<String, YALPComponent> byId = new LinkedHashMap<>();
    private final Map<Class<?>, YALPComponent> byClass = new HashMap<>();
    private final Map<String, ComponentLifecycle> lifecycle = new HashMap<>();
    private final YALPLogger logger;

    public ComponentManager(YALPLogger logger) {
        this.logger = logger;
    }

    @Override
    public void register(YALPComponent component) {
        String id = normalize(component.getId());
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Component id cannot be empty.");
        }
        if (byId.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate YALP component id: " + id);
        }
        byId.put(id, component);
        byClass.put(component.getClass(), component);
        lifecycle.put(id, ComponentLifecycle.REGISTERED);
    }

    public void loadAll(ComponentContext context) {
        for (YALPComponent component : sortedComponents()) {
            String id = normalize(component.getId());
            try {
                component.onLoad(context);
                lifecycle.put(id, ComponentLifecycle.LOADED);
                logger.info("Component loaded: " + id);
            } catch (RuntimeException exception) {
                logger.error("Component failed to load: " + id, exception);
            }
        }
    }

    public void enableAll() {
        for (YALPComponent component : sortedComponents()) {
            String id = normalize(component.getId());
            if (lifecycle.get(id) != ComponentLifecycle.LOADED) {
                continue;
            }
            try {
                component.onEnable();
                lifecycle.put(id, ComponentLifecycle.ENABLED);
            } catch (RuntimeException exception) {
                logger.error("Component failed to enable: " + id, exception);
            }
        }
    }

    public void reloadAll() {
        for (YALPComponent component : sortedComponents()) {
            try {
                component.reload();
            } catch (RuntimeException exception) {
                logger.error("Component failed to reload: " + component.getId(), exception);
            }
        }
    }

    public void disableAll() {
        List<YALPComponent> components = sortedComponents();
        Collections.reverse(components);
        for (YALPComponent component : components) {
            String id = normalize(component.getId());
            try {
                component.onDisable();
            } catch (RuntimeException exception) {
                logger.error("Component failed to disable cleanly: " + id, exception);
            } finally {
                lifecycle.put(id, ComponentLifecycle.DISABLED);
            }
        }
    }

    @Override
    public <T extends YALPComponent> Optional<T> get(Class<T> componentType) {
        YALPComponent exact = byClass.get(componentType);
        if (exact != null) {
            return Optional.of(componentType.cast(exact));
        }
        for (YALPComponent component : byId.values()) {
            if (componentType.isInstance(component)) {
                return Optional.of(componentType.cast(component));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<YALPComponent> get(String id) {
        return Optional.ofNullable(byId.get(normalize(id)));
    }

    @Override
    public boolean contains(String id) {
        return byId.containsKey(normalize(id));
    }

    @Override
    public Collection<YALPComponent> all() {
        return Collections.unmodifiableCollection(byId.values());
    }

    private List<YALPComponent> sortedComponents() {
        List<YALPComponent> sorted = new ArrayList<>();
        List<YALPComponent> remaining = new ArrayList<>(byId.values());
        while (!remaining.isEmpty()) {
            int before = remaining.size();
            for (int i = 0; i < remaining.size(); i++) {
                YALPComponent component = remaining.get(i);
                if (dependenciesSatisfied(component, sorted)) {
                    sorted.add(component);
                    remaining.remove(i);
                    i--;
                }
            }
            if (remaining.size() == before) {
                throw new IllegalStateException("Unresolved YALP component dependencies: " + remaining);
            }
        }
        return sorted;
    }

    private boolean dependenciesSatisfied(YALPComponent component, List<YALPComponent> sorted) {
        for (String dependency : component.getDependencies()) {
            String id = normalize(dependency);
            if (!byId.containsKey(id)) {
                throw new IllegalStateException("Component " + component.getId() + " depends on missing component " + id);
            }
            boolean found = false;
            for (YALPComponent loaded : sorted) {
                if (normalize(loaded.getId()).equals(id)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    private String normalize(String id) {
        return id == null ? "" : id.trim().toLowerCase(Locale.ROOT);
    }
}
