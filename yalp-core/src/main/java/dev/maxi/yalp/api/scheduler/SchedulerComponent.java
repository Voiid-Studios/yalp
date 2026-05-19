package dev.maxi.yalp.api.scheduler;

import dev.maxi.yalp.api.component.ComponentContext;
import dev.maxi.yalp.api.component.YALPComponent;
import dev.maxi.yalp.api.platform.ServerCompatibility;
import dev.maxi.yalp.api.util.YALPLogger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public final class SchedulerComponent implements YALPComponent {
    private Plugin plugin;
    private YALPLogger logger;
    private ServerCompatibility compatibility;

    @Override
    public String getId() {
        return "scheduler";
    }

    @Override
    public void onLoad(ComponentContext context) {
        this.plugin = context.getPlugin();
        this.logger = context.getLogger();
        this.compatibility = context.getCompatibility();
        if (compatibility.isFolia()) {
            logger.info("Folia detected, scheduler compatibility enabled.");
        } else if (compatibility.isLegacy()) {
            logger.info("Legacy server detected, using compatibility mode.");
        }
    }

    public YALPTask runSync(Runnable runnable) {
        if (compatibility.isFolia()) {
            return runFoliaGlobal(runnable, "run", new Class<?>[]{Plugin.class, Consumer.class}, plugin, consumer(runnable));
        }
        return wrap(Bukkit.getScheduler().runTask(plugin, runnable));
    }

    public YALPTask runAsync(Runnable runnable) {
        if (compatibility.isFolia()) {
            return runFoliaAsync(runnable, "runNow", new Class<?>[]{Plugin.class, Consumer.class}, plugin, consumer(runnable));
        }
        return wrap(Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable));
    }

    public YALPTask runLater(Runnable runnable, long delayTicks) {
        if (compatibility.isFolia()) {
            return runFoliaGlobal(runnable, "runDelayed", new Class<?>[]{Plugin.class, Consumer.class, long.class}, plugin, consumer(runnable), delayTicks);
        }
        return wrap(Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks));
    }

    public YALPTask runTimer(Runnable runnable, long delayTicks, long periodTicks) {
        if (compatibility.isFolia()) {
            return runFoliaGlobal(runnable, "runAtFixedRate", new Class<?>[]{Plugin.class, Consumer.class, long.class, long.class}, plugin, consumer(runnable), delayTicks, periodTicks);
        }
        return wrap(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delayTicks, periodTicks));
    }

    public YALPTask runAtEntity(Entity entity, Runnable runnable) {
        if (entity == null) {
            return runSync(runnable);
        }
        if (!compatibility.isFolia()) {
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

    public YALPTask runRegion(Location location, Runnable runnable) {
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

    public boolean isFolia() {
        return compatibility != null && compatibility.isFolia();
    }

    private Consumer<Object> consumer(Runnable runnable) {
        return ignored -> runnable.run();
    }

    private YALPTask runFoliaGlobal(Runnable fallback, String methodName, Class<?>[] parameterTypes, Object... arguments) {
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

    private YALPTask runFoliaAsync(Runnable fallback, String methodName, Class<?>[] parameterTypes, Object... arguments) {
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

    private YALPTask wrap(BukkitTask task) {
        return task::cancel;
    }

    private YALPTask wrapReflective(Object task) {
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
