# Folia Support

Folia splits server work across region-aware schedulers. Code that assumes one global main thread can break when it touches entities, chunks, or worlds.

YALP's `SchedulerComponent` detects Folia with runtime class checks. It does not compile against Folia APIs directly.

## Methods

- `runSync(Runnable)`: Bukkit main task on classic servers, global region scheduler on Folia.
- `runAsync(Runnable)`: Bukkit async scheduler or Folia async scheduler.
- `runLater(Runnable, ticks)`: delayed task through the safest available scheduler.
- `runTimer(Runnable, delay, period)`: repeating task through the safest available scheduler.
- `runAtEntity(Entity, Runnable)`: Folia entity scheduler when available, otherwise sync fallback.
- `runRegion(Location, Runnable)`: Folia region scheduler when available, otherwise sync fallback.

## Safe Usage

On Folia, prefer entity or region scheduling when touching:

- entity state
- block state
- chunk or world data
- inventories tied to a player or region

For pure logging, cache cleanup, web requests, or cooldown cleanup, async/global scheduling is usually fine.

## Limitations

Reflection keeps the jar compatible with older Spigot servers, but it also means YALP only exposes conservative scheduler behavior. Future versions can add richer Folia-specific utilities without making Folia a hard dependency.

