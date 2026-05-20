# Platform Adapter

`PlatformAdapter` keeps platform details out of core.

```java
PlatformAdapter adapter = yalp.platformAdapter();
adapter.getPlatformType();
adapter.getPlatformInfo();
adapter.getDataFolder();
adapter.getScheduler();
adapter.isInitialized();
adapter.isShuttingDown();
```

Bukkit implements this as `BukkitPlatformAdapter`. Future Velocity and BungeeCord modules should implement the same contract.

