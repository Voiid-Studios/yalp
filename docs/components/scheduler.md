# Scheduler Component

`SchedulerComponent` wraps Bukkit scheduling and uses reflection for Folia schedulers when available.

```java
yalp.scheduler().runSync(() -> {
    // classic Bukkit sync work
});

yalp.scheduler().runAsync(() -> {
    // database or file work
});

YALPTask task = yalp.scheduler().runLater(() -> {
    // delayed task
}, 20L);

task.cancel();
```

Folia-aware examples:

```java
yalp.scheduler().runAtEntity(player, () -> {
    // safer entity task on Folia when possible
});

yalp.scheduler().runAtLocation(location, () -> {
    // region scheduler on Folia when possible
});
```

On Folia, prefer entity or region methods when touching world, entity, player, chunk, or inventory state.

