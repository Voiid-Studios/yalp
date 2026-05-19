# GUI Component

`GuiComponent` is a tiny inventory helper, not a full menu framework.

```java
YALPGui gui = yalp.guis()
        .create("&8Example Menu", 27)
        .button(13, yalp.items()
                .material("DIAMOND")
                .name("&aClick me")
                .build(), event -> {
            event.getWhoClicked().sendMessage("Clicked!");
        })
        .build();

gui.open(player);
```

Clicks are cancelled by default to prevent item stealing:

```java
yalp.guis().create("&8Menu", 27)
        .cancelClicks(false)
        .build();
```

The implementation uses Bukkit inventory holders and one listener, so it works on legacy Bukkit.

