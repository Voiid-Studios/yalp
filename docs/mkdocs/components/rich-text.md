# Rich Text

Core defines a simple rich text model. Bukkit sends a safe colored-text fallback and keeps hover/click metadata in the model for future richer implementations.

```java
RichText text = yalp.richText()
        .text("Open menu")
        .color("&a")
        .hover("&7Click to open")
        .clickRunCommand("/menu")
        .build();

yalp.richText().send(player, text);
yalp.richText().send(player, yalp.richText().rainbow("YALP moment"));
```

Adventure is not required. Old servers safely receive plain colored text.

