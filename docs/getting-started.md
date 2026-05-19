# Getting Started

## Install YALP

Build the project and place `bukkit/target/YALP-1.0.0.jar` into your server `plugins/` folder. Restart the server.

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
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>voiidstudios</groupId>
    <artifactId>yalp-bukkit</artifactId>
    <version>1.0.0</version>
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
```

Bukkit-specific helpers are available by casting to `BukkitYALPApi`:

```java
BukkitYALPApi yalp = (BukkitYALPApi) YALPProvider.get();
yalp.messages().send(player, "{prefix} &aReady.");
```

For a safer optional lookup:

```java
yalp.getComponent(MessagesComponent.class).ifPresent(messages ->
        messages.send(sender, "&aMessages component is available."));
```
