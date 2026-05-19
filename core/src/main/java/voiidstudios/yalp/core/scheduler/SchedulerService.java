package voiidstudios.yalp.core.scheduler;

public interface SchedulerService {
    ScheduledTask runSync(Runnable runnable);

    ScheduledTask runAsync(Runnable runnable);

    ScheduledTask runLater(Runnable runnable, long delayTicks);

    ScheduledTask runTimer(Runnable runnable, long delayTicks, long periodTicks);
}
