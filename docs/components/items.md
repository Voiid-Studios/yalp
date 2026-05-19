# Item And Material Helpers

`BukkitItemBuilder` provides version-safe material resolution and item building in the Bukkit module.

```java
Optional<ItemStack> head = yalp.items().create("PLAYER_HEAD");
Optional<ItemStack> pane = yalp.items().create("GRAY_STAINED_GLASS_PANE", 1);
```

Common legacy mappings include:

- `PLAYER_HEAD` -> `SKULL_ITEM`, data `3`
- `WHITE_STAINED_GLASS_PANE` -> `STAINED_GLASS_PANE`, data `0`
- `GRAY_STAINED_GLASS_PANE` -> `STAINED_GLASS_PANE`, data `7`
- `OAK_SIGN` -> `SIGN`
- `EXPERIENCE_BOTTLE` -> `EXP_BOTTLE`

Builder example:

```java
ItemStack item = yalp.items()
        .material("PLAYER_HEAD")
        .name("&aExample Head")
        .lore("&7Works on old and new names where possible.")
        .build();
```

Unknown materials return `Optional.empty()` from `create`. Builder fallback uses stone and logs only in debug mode.
