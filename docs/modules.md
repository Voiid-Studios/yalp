# Modules

YALP features are organized as components. A component has an id, lifecycle methods, and optional dependencies on other YALP components.

## Lifecycle

```java
public interface YALPComponent {
    String getId();
    default List<String> getDependencies();
    default void onLoad(ComponentContext context);
    default void onEnable();
    default void onDisable();
    default void reload();
}
```

- `onLoad`: capture plugin context, prepare files, detect platform features.
- `onEnable`: start tasks or register runtime services.
- `onDisable`: cancel tasks and release resources.
- `reload`: reload component-owned config or runtime state.

## Creating A Component

```java
public final class MyComponent implements YALPComponent {
    @Override
    public String getId() {
        return "my-component";
    }

    @Override
    public List<String> getDependencies() {
        return Collections.singletonList("messages");
    }

    @Override
    public void onLoad(ComponentContext context) {
        context.getLogger().info("My component loaded.");
    }
}
```

Register internal components in `YALPPlugin#onLoad`. The registry prevents duplicate ids and duplicate component classes.

## Future Folder Structure

Recommended package layout for new components:

```text
dev.maxi.yalp.api.<component>        public component API
dev.maxi.yalp.internal.<component>   private implementation helpers
```

Keep public APIs small. Put server-version tricks and reflection in internal helpers when a component grows.

