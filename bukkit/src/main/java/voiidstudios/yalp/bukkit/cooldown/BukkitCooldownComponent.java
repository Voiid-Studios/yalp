package voiidstudios.yalp.bukkit.cooldown;

import voiidstudios.yalp.core.component.ComponentContext;
import voiidstudios.yalp.core.component.YALPComponent;
import voiidstudios.yalp.core.cooldown.DefaultCooldownService;
import voiidstudios.yalp.core.scheduler.ScheduledTask;

import java.util.Collections;
import java.util.List;

public final class BukkitCooldownComponent extends DefaultCooldownService implements YALPComponent {
    private ScheduledTask cleanupTask;

    @Override
    public String getId() {
        return "cooldown";
    }

    @Override
    public List<String> getDependencies() {
        return Collections.singletonList("scheduler");
    }

    @Override
    public void onLoad(ComponentContext context) {
        cleanupTask = context.getApi().scheduler().runTimer(this::cleanupExpired, 20L * 60L, 20L * 60L);
    }

    @Override
    public void onDisable() {
        if (cleanupTask != null) {
            cleanupTask.cancel();
        }
    }
}
