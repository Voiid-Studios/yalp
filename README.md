# YALP

**Yet Another Library Plugin**

> yup, another one ¯\\_(ツ)_/¯

YALP is a small shared utility library plugin for Bukkit, Spigot, Paper, and Folia servers. It exists so your own plugins can depend on one reusable base instead of copying message helpers, config wrappers, cooldown maps, scheduler glue, and item builders into every project.

The first version is intentionally simple: a normal Bukkit plugin, a stable public API, a component registry, and a few practical components.

## Compatibility

- Targets Java 8 bytecode.
- Compiles against Bukkit API 1.8.8 for broad legacy compatibility.
- Avoids `api-version` in `plugin.yml` so Minecraft 1.8 servers do not reject the plugin metadata.
- Does not depend on PlaceholderAPI, Vault, ProtocolLib, Paper, or Folia.
- Paper and Folia APIs are detected at runtime and accessed through compatibility wrappers.

## Installation

1. Build YALP with Gradle.
2. Put `YALP-1.0.0.jar` in your server `plugins/` folder.
3. Restart the server.
4. Add `depend: [YALP]` to plugins that require it.

## Build

```bash
./gradlew build
```

On Windows:

```powershell
gradle build
```

Artifacts are written to:

- `yalp-core/build/libs/YALP-1.0.0.jar`
- `examples/ExampleYALPPlugin/build/libs/ExampleYALPPlugin-1.0.0.jar`

## Dependency Setup

For another Gradle project, depend on the YALP API jar as `compileOnly`:

```groovy
dependencies {
    compileOnly files("libs/YALP-1.0.0.jar")
    compileOnly "org.bukkit:bukkit:1.8.8-R0.1-SNAPSHOT"
}
```

In `plugin.yml`:

```yaml
depend: [YALP]
```

Use `depend` when your plugin cannot run without YALP. Use `softdepend` only if your plugin has a fallback path when YALP is missing.

## API Usage

```java
YALPApi yalp = YALPProvider.get();
MessagesComponent messages = yalp.getComponent(MessagesComponent.class).orElseThrow(
        () -> new IllegalStateException("YALP messages component is missing"));

messages.send(sender, "{prefix} &aHello from YALP.");
```

Convenience methods are also available:

```java
yalp.messages().send(sender, "&aYup.");
yalp.cooldowns().setCooldown(player.getUniqueId(), "example", Duration.ofSeconds(5));
yalp.scheduler().runLater(() -> getLogger().info("Later."), 20L);
```

## Components

- `messages`: color codes and basic `{placeholder}` replacement.
- `config`: YAML loading, saving, and reloading helpers.
- `scheduler`: Bukkit/Paper/Folia-aware task scheduling facade.
- `cooldown`: UUID and key based cooldown manager.
- `itembuilder`: legacy-compatible `ItemStack` builder.

## Folia Notes

Folia has different threading rules than classic Bukkit. YALP detects Folia at runtime and uses reflection to call region, entity, async, and global schedulers where available. `runSync` means "run through the safest available server scheduler", not "force this exact thread".

Prefer `runAtEntity` or `runRegion` when the task touches entity or world state on Folia.

## Legacy Notes

YALP compiles against Spigot 1.8.8. Modern-only APIs should stay behind reflection or version checks. If a future component needs newer server APIs, put that behavior behind a compatibility wrapper.

## Reload Warning

Runtime reload tools can leave dependency plugins in a weird state. If YALP sees a reload-like lifecycle, it prints:

```text
*****************************************************************
*** WARNING                                                   ***
*** RELOADING THE SERVER WHILE YALP IS ENABLED MIGHT          ***
*** LEAD TO UNEXPECTED ERRORS.                                ***
***                                                           ***
*** Please restart the server cleanly before blaming YALP.     ***
*****************************************************************
[YALP] another reload ¯\_(ツ)_/¯
```

Restart the server cleanly before debugging dependency weirdness.

## MiniBoot Pattern

YALP does not auto-download itself. Other plugins may optionally implement a transparent "MiniBoot" bootstrap, but it should be explicit, logged, configurable, and one-time only.

Recommended behavior:

- If YALP is missing on first install, download it only from a trusted configured URL.
- Write `plugins/<PluginName>/miniboot.yml` with `once-installed: true`.
- Tell the admin to restart.
- If the admin later removes YALP, do not reinstall automatically.
- Print a clear error asking the admin to reinstall YALP manually.
- Avoid hot-loading YALP unless you have verified it is safe for that server.

See `docs/dependency-setup.md` for pseudocode.

## Roadmap

- Add optional PlaceholderAPI/Vault hooks as separate components.
- Add versioned material helpers for legacy and modern item names.
- Add command, inventory, and database utility components.
- Publish API artifacts to a Maven repository.
- Add tests around component dependency ordering and cooldown behavior.
