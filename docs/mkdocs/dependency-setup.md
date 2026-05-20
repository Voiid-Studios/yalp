# Dependency Setup

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

Replace `VERSION` with the latest GitHub release tag.

Use the local Maven reactor, `mvn install`, or your own Maven repository until YALP is published.

## JitPack

If YALP is published through JitPack later, add:

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

Then use the latest GitHub release tag as `VERSION`.

## plugin.yml

```yaml
depend: [YALP]
```

Use `depend` when YALP is required. Bukkit will refuse to load your plugin if YALP is missing, which is clearer than a random `ClassNotFoundException`.

Use `softdepend` only when your plugin can run without YALP:

```yaml
softdepend: [YALP]
```

## Optional MiniBoot Pseudocode

Do not put auto-install logic inside YALP. If another plugin uses MiniBoot, keep it transparent:

```java
if (!isPluginInstalled("YALP")) {
    MiniBootState state = loadMiniBootState();
    if (state.onceInstalled()) {
        logSevere("YALP was removed. Please reinstall YALP manually and restart.");
        disablePlugin();
        return;
    }

    if (!config.getBoolean("miniboot.enabled")) {
        logSevere("YALP is missing. Install YALP and restart.");
        disablePlugin();
        return;
    }

    logWarning("YALP is missing. Downloading from configured trusted URL.");
    download(config.getString("miniboot.yalp-url"), "plugins/YALP.jar");
    state.setOnceInstalled(true);
    state.save();
    logWarning("YALP downloaded. Restart the server to finish installation.");
    disablePlugin();
}
```

Never silently download dependencies, never repeatedly reinstall after admin removal, and prefer restart over hot-loading.
