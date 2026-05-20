package voiidstudios.yalp.bukkit.platform;

import org.bukkit.plugin.java.JavaPlugin;
import voiidstudios.yalp.core.logging.YALPLogger;
import voiidstudios.yalp.core.platform.PlatformAdapter;
import voiidstudios.yalp.core.platform.PlatformInfo;
import voiidstudios.yalp.core.platform.PlatformLifecycleState;
import voiidstudios.yalp.core.platform.PlatformType;
import voiidstudios.yalp.core.scheduler.SchedulerService;

import java.io.File;

public final class BukkitPlatformAdapter implements PlatformAdapter {
    private final JavaPlugin plugin;
    private final PlatformInfo platformInfo;
    private final YALPLogger logger;
    private final SchedulerService scheduler;
    private PlatformLifecycleState lifecycleState = PlatformLifecycleState.CREATED;

    public BukkitPlatformAdapter(JavaPlugin plugin, PlatformInfo platformInfo, YALPLogger logger, SchedulerService scheduler) {
        this.plugin = plugin;
        this.platformInfo = platformInfo;
        this.logger = logger;
        this.scheduler = scheduler;
    }

    @Override
    public PlatformType getPlatformType() { return platformInfo.getType(); }

    @Override
    public PlatformInfo getPlatformInfo() { return platformInfo; }

    @Override
    public YALPLogger getLogger() { return logger; }

    @Override
    public File getDataFolder() { return plugin.getDataFolder(); }

    @Override
    public SchedulerService getScheduler() { return scheduler; }

    @Override
    public PlatformLifecycleState getLifecycleState() { return lifecycleState; }

    public void setLifecycleState(PlatformLifecycleState lifecycleState) { this.lifecycleState = lifecycleState; }

    @Override
    public boolean isInitialized() {
        return lifecycleState == PlatformLifecycleState.INITIALIZED || lifecycleState == PlatformLifecycleState.ENABLED;
    }

    @Override
    public boolean isShuttingDown() { return lifecycleState == PlatformLifecycleState.SHUTTING_DOWN; }
}
