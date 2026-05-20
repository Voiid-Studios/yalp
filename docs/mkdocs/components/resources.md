# Resources

The resource component reads and copies bundled plugin resources.

```java
yalp.resources().copyDefault(plugin, "config.yml");
yalp.resources().copyDefault(plugin, "lang/en_US.yml");
Optional<String> text = yalp.resources().readText(plugin, "defaults/example.txt");
```

Resources are read as UTF-8 and streams are closed safely. Missing resources return empty optionals or `false`.

