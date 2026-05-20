# JSON Files

The JSON file component is for small structured files where JSON is nicer than YAML.

```java
JsonFile<MySettings> settings = yalp.jsonFiles().register(
        plugin,
        "settings",
        new File(plugin.getDataFolder(), "settings.json"),
        MySettings.class,
        MySettings::new
);

settings.load();
settings.save();
```

Invalid JSON is logged, moved aside as a `.broken` file, and replaced with defaults. Gson is shaded into the Bukkit plugin jar.

Use YAML for admin-facing configs and JSON for small structured data objects.

