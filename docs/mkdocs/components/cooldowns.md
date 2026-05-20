# Cooldown Component

`CooldownComponent` supports UUID cooldowns, global keyed cooldowns, automatic cleanup, and readable remaining time.

```java
yalp.cooldowns().setCooldown(player.getUniqueId(), "kit", Duration.ofSeconds(30));

if (yalp.cooldowns().hasCooldown(player.getUniqueId(), "kit")) {
    String left = yalp.cooldowns().formatRemaining(player.getUniqueId(), "kit");
    yalp.messages().send(player, "&cWait " + left + ".");
}
```

Global cooldown:

```java
yalp.cooldowns().setCooldown("broadcast", Duration.ofMinutes(1));
boolean active = yalp.cooldowns().hasCooldown("broadcast");
```

Expired entries are cleaned periodically through YALP's scheduler.

