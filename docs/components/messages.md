# Messages Component

`MessagesComponent` is intentionally small: colors, placeholders, lines, prefixes, and a few utilities.

```java
yalp.messages().send(player, "{prefix} &aHello {player}!", "player", player.getName());
```

```java
Map<String, String> placeholders = new HashMap<>();
placeholders.put("player", player.getName());
yalp.messages().send(player, "{prefix} &eWelcome, {player}.", placeholders);
```

```java
yalp.messages().sendLines(player, Arrays.asList("&7Line 1", "&aLine 2"));
String clean = yalp.messages().stripColors("&aGreen");
String centered = yalp.messages().center("&eTitle");
```

MiniMessage is only detected when Adventure is already present. YALP does not depend on Adventure.

