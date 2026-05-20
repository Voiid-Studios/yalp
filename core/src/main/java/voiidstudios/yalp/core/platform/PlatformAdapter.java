package voiidstudios.yalp.core.platform;

import voiidstudios.yalp.core.logging.YALPLogger;
import voiidstudios.yalp.core.scheduler.SchedulerService;

import java.io.File;

public interface PlatformAdapter {
    PlatformType getPlatformType();

    PlatformInfo getPlatformInfo();

    YALPLogger getLogger();

    File getDataFolder();

    SchedulerService getScheduler();

    PlatformLifecycleState getLifecycleState();

    boolean isInitialized();

    boolean isShuttingDown();
}
