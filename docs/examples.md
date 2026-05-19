# Examples

## Messages

```java
yalp.messages().send(sender, "{prefix} &aHello {player}.");
```

```java
Map<String, String> placeholders = new HashMap<>();
placeholders.put("player", player.getName());
String text = yalp.messages().format("&aWelcome {player}.", placeholders);
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

## Example Plugin

See `examples/ExampleYALPPlugin` for a complete dependent plugin with `depend: [YALP]`, config loading, cooldown command usage, and scheduler usage.

