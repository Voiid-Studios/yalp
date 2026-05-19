# Dependency Setup

## Gradle

```groovy
repositories {
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
}

dependencies {
    compileOnly files("libs/YALP-1.0.0.jar")
    compileOnly "org.bukkit:bukkit:1.8.8-R0.1-SNAPSHOT"
}
```

## Maven

```xml
<dependency>
    <groupId>dev.maxi.yalp</groupId>
    <artifactId>YALP</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
```

Use the local jar or your own Maven repository until YALP is published.

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
