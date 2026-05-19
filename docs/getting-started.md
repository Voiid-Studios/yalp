# Getting Started

## Install YALP

Build the project and place `yalp-core/build/libs/YALP-1.0.0.jar` into your server `plugins/` folder. Restart the server.

YALP should print messages like:

```text
[YALP] Component loaded: messages
[YALP] Component loaded: config
[YALP] Component loaded: scheduler
[YALP] yup
```

## Add YALP To Another Plugin

Add the YALP jar to your compile classpath and mark it as `compileOnly`. Do not shade YALP into every plugin; the point is to have one shared dependency plugin.

```groovy
dependencies {
    compileOnly files("libs/YALP-1.0.0.jar")
}
```

In your plugin's `plugin.yml`:

```yaml
depend: [YALP]
```

## Access Components

```java
YALPApi yalp = YALPProvider.get();
yalp.messages().send(sender, "{prefix} &aReady.");
```

Useful convenience methods:

```java
yalp.compatibility();
yalp.messages();
yalp.configs();
yalp.scheduler();
yalp.cooldowns();
yalp.items();
yalp.hooks();
yalp.guis();
yalp.logger();
```

For a safer optional lookup:

```java
yalp.getComponent(MessagesComponent.class).ifPresent(messages ->
        messages.send(sender, "&aMessages component is available."));
```
