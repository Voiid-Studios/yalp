# Translations

Core defines `TranslationService` and `TranslationContext`. Bukkit loads YAML language files from plugin data folders and lets plugins create their own translation contexts.

```java
TranslationContext context = yalp.translations().createContext(
        plugin,
        "lang",
        "en_US",
        "es_CL"
);

String text = context.get("commands.cooldown", "time", "5s");
context.reload();
```

Missing keys return visible text like `<missing translation: commands.example>` and debug logs are only printed when debug mode is enabled.

