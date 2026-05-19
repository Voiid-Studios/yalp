# Examples

## Messages

```java
yalp.messages().send(player, "{prefix} &aHello {player}!", "player", player.getName());
```

```java
Map<String, String> placeholders = new HashMap<>();
placeholders.put("player", player.getName());
String text = yalp.messages().format("&aWelcome {player}.", placeholders);
```

```java
yalp.messages().sendLines(player, Arrays.asList("&7Line 1", "&aLine 2"));
yalp.messages().center("&eCentered title");
yalp.messages().stripColors("&aNo color left.");
```

## Compatibility

```java
if (yalp.compatibility().isFolia()) {
    yalp.logger().info("Folia detected.");
}

yalp.logger().info("Server: " + yalp.compatibility().getServerSoftware());
yalp.logger().info("Minecraft: " + yalp.compatibility().getMinecraftVersion());
```

## Config

```java
FileConfiguration config = yalp.configs().load(this, "config.yml");
String message = config.getString("message", "{prefix} &aReady.");
yalp.configs().save(this, "config.yml");
```

## Scheduler

```java
yalp.scheduler().runLater(() ->
        getLogger().info("Ran after one second."), 20L);
```

```java
yalp.scheduler().runAtEntity(player, () ->
        player.sendMessage("Safe entity scheduled task."));
```

```java
YALPTask task = yalp.scheduler().runTimer(() ->
        getLogger().info("Repeating task."), 20L, 20L);
task.cancel();
```

## Cooldowns

```java
UUID id = player.getUniqueId();
if (yalp.cooldowns().hasCooldown(id, "command")) {
    long seconds = yalp.cooldowns().getRemaining(id, "command").getSeconds();
    yalp.messages().send(player, "&cWait " + seconds + "s.");
    return;
}

yalp.cooldowns().setCooldown(id, "command", Duration.ofSeconds(5));
```

```java
yalp.cooldowns().setCooldown("global-broadcast", Duration.ofMinutes(1));
String remaining = yalp.cooldowns().formatRemaining("global-broadcast");
```

## Item Builder

```java
ItemStack item = yalp.itemBuilder()
        .item(Material.DIAMOND_SWORD)
        .amount(1)
        .name("§bExample Sword")
        .lore("§7Built by YALP.")
        .durability((short) 0)
        .flags("HIDE_ATTRIBUTES", "HIDE_ENCHANTS")
        .build();
```

```java
yalp.items().create("PLAYER_HEAD").ifPresent(item ->
        player.getInventory().addItem(item));
```

## Hooks

```java
if (yalp.hooks().hasPlaceholderAPI()) {
    yalp.logger().info("PlaceholderAPI detected.");
}
```

## GUI

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

## Example Plugin

See `examples/ExampleYALPPlugin` for a complete dependent plugin with `depend: [YALP]`, config loading, compatibility info, hooks, items, GUI, cooldown command usage, and scheduler usage.
