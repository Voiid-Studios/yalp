# YAML Folder Registry

The YAML folder registry loads modular `.yml` definition files from a plugin folder.

```java
YamlDefinitionRegistry registry = yalp.yamlFolders().load(plugin, "examples", "rewards");
registry.get("welcome").ifPresent(definition -> {
    boolean enabled = definition.isEnabled();
});
registry.setEnabled("welcome", false);
registry.save("welcome");
```

Invalid files are logged and skipped. One broken definition should not stop the whole plugin.

