# Getting Started

## Install YALP

Build the project and place `bukkit/target/YALP-VERSION.jar` into your server `plugins/` folder. Replace `VERSION` with the latest GitHub release tag. Restart the server.

YALP should print messages like:

```text
[YALP] Component loaded: messages
[YALP] Component loaded: config
[YALP] Component loaded: scheduler
[YALP] yup
```

## Add YALP To Another Plugin

Add YALP as provided Maven dependencies. Do not shade YALP into every plugin; the point is to have one shared dependency plugin.

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

In your plugin's `plugin.yml`:

```yaml
depend: [YALP]
```

## Access Components

```java
YALPApi yalp = YALPProvider.get();
String message = yalp.messages().format("{prefix} &aReady.");
yalp.cooldowns().setCooldown(playerId, "kit", Duration.ofSeconds(30));
yalp.translations();
yalp.resources();
yalp.richText();
```

Bukkit-specific helpers are available by casting to `BukkitYALPApi`:

```java
BukkitYALPApi yalp = (BukkitYALPApi) YALPProvider.get();
yalp.messages().send(player, "{prefix} &aReady.");
yalp.items().create("PLAYER_HEAD");
yalp.guis().create("&8Menu", 27);
```

For a safer optional lookup:

```java
yalp.getComponent(YALPComponent.class).ifPresent(component ->
        yalp.logger().info("Found component: " + component.getId()));
```
