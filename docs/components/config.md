# Config Component

`ConfigComponent` loads, saves, and reloads YAML files using Bukkit's legacy-safe configuration API.

```java
FileConfiguration config = yalp.configs().load(this, "config.yml");
String value = config.getString("message", "{prefix} &aReady.");
yalp.configs().save(this, "config.yml");
```

If a bundled resource exists, YALP can copy it. If not, loading still returns an empty YAML configuration.

