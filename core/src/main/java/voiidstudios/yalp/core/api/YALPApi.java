package voiidstudios.yalp.core.api;

import voiidstudios.yalp.core.compatibility.CompatibilityService;
import voiidstudios.yalp.core.component.YALPComponent;
import voiidstudios.yalp.core.component.registry.ComponentRegistry;
import voiidstudios.yalp.core.config.ConfigService;
import voiidstudios.yalp.core.cooldown.CooldownService;
import voiidstudios.yalp.core.hooks.HooksService;
import voiidstudios.yalp.core.logging.YALPLogger;
import voiidstudios.yalp.core.messages.MessageFormatter;
import voiidstudios.yalp.core.platform.PlatformInfo;
import voiidstudios.yalp.core.scheduler.SchedulerService;

import java.util.Optional;

public interface YALPApi {
    String getVersion();

    PlatformInfo platform();

    ComponentRegistry getComponentRegistry();

    <T extends YALPComponent> Optional<T> getComponent(Class<T> componentType);

    Optional<YALPComponent> getComponent(String id);

    YALPLogger logger();

    MessageFormatter messages();

    CooldownService cooldowns();

    SchedulerService scheduler();

    ConfigService configs();

    HooksService hooks();

    CompatibilityService compatibility();
}
