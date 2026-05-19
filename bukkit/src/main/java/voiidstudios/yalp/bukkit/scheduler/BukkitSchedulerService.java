package voiidstudios.yalp.bukkit.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import voiidstudios.yalp.bukkit.compatibility.BukkitCompatibilityComponent;
import voiidstudios.yalp.core.component.ComponentContext;
import voiidstudios.yalp.core.component.YALPComponent;
import voiidstudios.yalp.core.logging.YALPLogger;
import voiidstudios.yalp.core.scheduler.ScheduledTask;
import voiidstudios.yalp.core.scheduler.SchedulerService;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public final class BukkitSchedulerService implements SchedulerService, YALPComponent {
    private final Plugin plugin;
    private final BukkitCompatibilityComponent compatibility;
    private final YALPLogger logger;

    public BukkitSchedulerService(Plugin plugin, BukkitCompatibilityComponent compatibility, YALPLogger logger) {
        this.plugin = plugin;
        this.compatibility = compatibility;
        this.logger = logger;
    }

    @Override
    public String getId() {
        return "scheduler";
    }

    @Override
    public void onLoad(ComponentContext context) {
        if (compatibility.isFolia()) {
            logger.info("Folia detected, scheduler compatibility enabled.");
        } else if (compatibility.isLegacy()) {
            logger.info("Legacy server detected, using compatibility mode.");
        }
    }

    @Override
    public ScheduledTask runSync(Runnable runnable) {
        if (compatibility.isFolia()) {
            return runFoliaGlobal(runnable, "run", new Class<?>[]{Plugin.class, Consumer.class}, plugin, consumer(runnable));
        }
        return wrap(Bukkit.getScheduler().runTask(plugin, runnable));
    }

    @Override
    public ScheduledTask runAsync(Runnable runnable) {
        if (compatibility.isFolia()) {
            return runFoliaAsync(runnable, "runNow", new Class<?>[]{Plugin.class, Consumer.class}, plugin, consumer(runnable));
        }
        return wrap(Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable));
    }

    @Override
    public ScheduledTask runLater(Runnable runnable, long delayTicks) {
        if (compatibility.isFolia()) {
            return runFoliaGlobal(runnable, "runDelayed", new Class<?>[]{Plugin.class, Consumer.class, long.class}, plugin, consumer(runnable), delayTicks);
        }
        return wrap(Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks));
    }

    @Override
    public ScheduledTask runTimer(Runnable runnable, long delayTicks, long periodTicks) {
        if (compatibility.isFolia()) {
            return runFoliaGlobal(runnable, "runAtFixedRate", new Class<?>[]{Plugin.class, Consumer.class, long.class, long.class}, plugin, consumer(runnable), delayTicks, periodTicks);
        }
        return wrap(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delayTicks, periodTicks));
    }

    public ScheduledTask runAtEntity(Entity entity, Runnable runnable) {
        if (entity == null || !compatibility.isFolia()) {
            return runSync(runnable);
        }
        try {
            Method getScheduler = entity.getClass().getMethod("getScheduler");
            Object scheduler = getScheduler.invoke(entity);
            Method run = scheduler.getClass().getMethod("run", Plugin.class, Consumer.class, Runnable.class);
            return wrapReflective(run.invoke(scheduler, plugin, consumer(runnable), null));
        } catch (ReflectiveOperationException exception) {
            logger.warn("Folia entity scheduler unavailable, falling back to global scheduler.");
            return runSync(runnable);
        }
    }

    public ScheduledTask runAtLocation(Location location, Runnable runnable) {
        if (location == null || !compatibility.isFolia()) {
            return runSync(runnable);
        }
        try {
            Method getRegionScheduler = Bukkit.class.getMethod("getRegionScheduler");
            Object scheduler = getRegionScheduler.invoke(null);
            Method run = scheduler.getClass().getMethod("run", Plugin.class, Location.class, Consumer.class);
            return wrapReflective(run.invoke(scheduler, plugin, location, consumer(runnable)));
        } catch (ReflectiveOperationException exception) {
            logger.warn("Folia region scheduler unavailable, falling back to global scheduler.");
            return runSync(runnable);
        }
    }

    private Consumer<Object> consumer(Runnable runnable) {
        return ignored -> runnable.run();
    }

    private ScheduledTask runFoliaGlobal(Runnable fallback, String methodName, Class<?>[] parameterTypes, Object... arguments) {
        try {
            Method schedulerMethod = Bukkit.class.getMethod("getGlobalRegionScheduler");
            Object scheduler = schedulerMethod.invoke(null);
            Method method = scheduler.getClass().getMethod(methodName, parameterTypes);
            return wrapReflective(method.invoke(scheduler, arguments));
        } catch (ReflectiveOperationException exception) {
            logger.warn("Folia global scheduler unavailable, falling back to Bukkit scheduler.");
            return wrap(Bukkit.getScheduler().runTask(plugin, fallback));
        }
    }

    private ScheduledTask runFoliaAsync(Runnable fallback, String methodName, Class<?>[] parameterTypes, Object... arguments) {
        try {
            Method schedulerMethod = Bukkit.class.getMethod("getAsyncScheduler");
            Object scheduler = schedulerMethod.invoke(null);
            Method method = scheduler.getClass().getMethod(methodName, parameterTypes);
            return wrapReflective(method.invoke(scheduler, arguments));
        } catch (ReflectiveOperationException exception) {
            logger.warn("Folia async scheduler unavailable, falling back to Bukkit async scheduler.");
            return wrap(Bukkit.getScheduler().runTaskAsynchronously(plugin, fallback));
        }
    }

    private ScheduledTask wrap(BukkitTask task) {
        return task::cancel;
    }

    private ScheduledTask wrapReflective(Object task) {
        return () -> {
            if (task == null) {
                return;
            }
            try {
                task.getClass().getMethod("cancel").invoke(task);
            } catch (ReflectiveOperationException ignored) {
            }
        };
    }
}
