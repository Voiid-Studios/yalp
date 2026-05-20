# YALP

**Yet Another Library Plugin**

> yup, another one ¯\\_(ツ)_/¯

YALP is a lightweight shared utility library for Minecraft plugins. It is now split into a platform-independent `core` module and a Bukkit-family implementation module.

Technically it is a plugin. Emotionally, it's a dependency.

## Modules

- `core`: platform-independent API contracts, component lifecycle, registry, logging abstraction, message formatting, cooldown logic, scheduler interfaces, and shared service interfaces.
- `bukkit`: Bukkit/Spigot/Paper/Purpur/Folia implementation. This is the actual server plugin jar.
- `examples/bukkit-example`: small Bukkit plugin showing how future plugins should integrate with YALP.

Future modules can be added beside these:

```text
velocity/
bungeecord/
```

Those modules should depend on `yalp-core` and implement the same core API for their platform.

## Build

```bash
mvn clean package
```

Artifacts:

- `core/target/yalp-core-VERSION.jar`
- `bukkit/target/YALP-VERSION.jar`
- `examples/bukkit-example/target/yalp-bukkit-example-VERSION.jar`

Install `bukkit/target/YALP-VERSION.jar` into your server `plugins/` folder. Replace `VERSION` with the latest GitHub release tag.

## Documentation

MkDocs source lives in:

```text
docs/mkdocs/
```

Build or serve it locally with:

```bash
mkdocs build
mkdocs serve
```

JavaDocs are separate API reference output:

```bash
mvn javadoc:aggregate
```

Generated JavaDocs are written to:

```text
target/site/apidocs/
```

Suggested GitHub Pages layout:

```text
/YALP/      -> MkDocs site
/YALP/jd/   -> JavaDocs API reference
```

## Compatibility

- Java 8 bytecode.
- Bukkit API is only a provided dependency in the `bukkit` module.
- `core` has no Bukkit, Paper, Folia, Velocity, or BungeeCord imports.
- Bukkit module compiles against Bukkit `1.8.8-R0.1-SNAPSHOT`.
- Paper, Purpur, and Folia are detected at runtime.
- Folia scheduling is isolated behind reflection-based wrappers.
- No hard dependency on PlaceholderAPI, Vault, ProtocolLib, LuckPerms, ItemsAdder, WorldEdit, or WorldGuard.
- `plugin.yml` intentionally avoids `api-version` for 1.8 compatibility.

## Dependency Setup

For another Bukkit plugin:

```xml
<dependency>
    <groupId>voiidstudios</groupId>
    <artifactId>yalp-core</artifactId>
    <version>VERSION</version>
    <scope>provided</scope>
</dependency>

<dependency>
    <groupId>voiidstudios</groupId>
    <artifactId>yalp-bukkit</artifactId>
    <version>VERSION</version>
    <scope>provided</scope>
</dependency>
```

In `plugin.yml`:

```yaml
depend: [YALP]
```

Use `depend` when your plugin requires YALP. Use `softdepend` only when your plugin can run without it.

## API Usage

Core access:

```java
YALPApi yalp = YALPProvider.get();
yalp.logger().info("yup");
yalp.messages().format("{prefix} &aHello {player}!", "player", "Maxi");
yalp.cooldowns().setCooldown(playerId, "kit", Duration.ofSeconds(30));
```

Bukkit-specific access:

```java
BukkitYALPApi yalp = (BukkitYALPApi) YALPProvider.get();
yalp.messages().send(player, "{prefix} &aHello {player}!", "player", player.getName());
yalp.items().create("PLAYER_HEAD").ifPresent(item -> player.getInventory().addItem(item));
```

## Components

Core/platform-neutral:

- Component registry
- Logger API and log levels
- Message formatter
- Translation API
- Cooldowns
- Platform adapter contracts
- Scheduler contracts
- YAML definition registry contracts
- Command contracts
- Resource, JSON, and rich text contracts
- Utility helpers

Bukkit-specific:

- Bukkit plugin implementation
- Bukkit scheduler/Folia scheduler
- Bukkit YAML configs
- Bukkit command sender messages
- Bukkit item compatibility
- Bukkit GUI utilities
- Bukkit hooks detection
- Bukkit translation file loading
- Bukkit YAML folder registries
- Bukkit command wrapper
- Bukkit resource copying
- Bukkit JSON files
- Bukkit rich text fallback sending

Replace `VERSION` with the latest GitHub release tag.

If using JitPack later, add the JitPack repository and use the release tag as `VERSION`.

Component docs live in `docs/mkdocs/components/`.

## Example Commands

The Bukkit example demonstrates:

- `/yalpexample help`
- `/yalpexample info`
- `/yalpexample logger`
- `/yalpexample lang`
- `/yalpexample lang reload`
- `/yalpexample definitions list`
- `/yalpexample definitions info <id>`
- `/yalpexample definitions toggle <id>`
- `/yalpexample resource`
- `/yalpexample json show`
- `/yalpexample json save`
- `/yalpexample json reload`
- `/yalpexample text`
- `/yalpexample rainbow`
- `/yalpexample cooldown`
- `/yalpexample gui`
- `/yalpexample hooks`
- `/yalpexample item`
- `/yalpexample scheduler`

## MiniBoot

YALP does not auto-install itself. Other plugins may document or implement an optional MiniBoot pattern, but it must be transparent, configurable, one-time only, and should tell the admin to restart. Do not silently download dependencies. Do not reinstall YALP after an admin removes it.

## Reload Warning

Runtime reload tools can leave dependency plugins in strange states. YALP keeps the warning:

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

## FAQ

**Is YALP a plugin?**  
Technically yes. Emotionally, it's a dependency.

**Why is the logo just ¯\\_(ツ)_/¯ ?**  
why not

**Can I use YALP in my own plugins?**  
yup

**Is this the Lua YALP?**  
No. This one is for Minecraft.

**Will YALP auto-install itself?**  
No.

## Roadmap

- Add optional platform modules for Velocity and BungeeCord.
- Add optional integrations as separate modules.
- Add more material mappings as needed.
- Publish artifacts to a Maven repository.
