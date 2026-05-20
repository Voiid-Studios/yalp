# Modules

YALP features are organized as components. Core defines the lifecycle and registry; platform modules register their own implementations.

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

Register internal components in the platform plugin lifecycle. The Bukkit module registers components in `BukkitYALPPlugin#onLoad`.

## Future Folder Structure

Recommended package layout for platform-neutral components:

```text
voiidstudios.yalp.core.<component>      public contract or shared implementation
voiidstudios.yalp.bukkit.<component>    Bukkit implementation
```

Keep public APIs small. Put server-version tricks and reflection in internal helpers when a component grows.

## Current Components

- `logger`: readable console output and debug mode.
- `translations`: core translation contracts, Bukkit YAML language files.
- `yaml-folders`: modular YAML definition registries.
- `commands`: small command wrapper for declared Bukkit commands.
- `resources`: bundled resource reading/copying.
- `json-files`: small JSON-backed file helpers.
- `rich-text`: platform-neutral text model and Bukkit fallback sending.
- `platform-adapter`: platform lifecycle and bridge contract.
- `compatibility`: best-effort server and feature checks.
- `messages`: colors, placeholders, lines, prefix utilities.
- `config`: Bukkit YAML helpers.
- `scheduler`: core interface, Bukkit/Folia implementation.
- `cooldown`: core logic, Bukkit cleanup scheduling.
- `items`: Bukkit item builder and material compatibility helpers.
- `hooks`: Bukkit optional plugin detection.
- `gui`: Bukkit minimal inventory menu helper.
