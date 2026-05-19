package voiidstudios.yalp.core.component;

import voiidstudios.yalp.core.api.YALPApi;
import voiidstudios.yalp.core.logging.YALPLogger;
import voiidstudios.yalp.core.platform.PlatformInfo;

public class ComponentContext {
    private final YALPApi api;
    private final YALPLogger logger;
    private final PlatformInfo platform;

    public ComponentContext(YALPApi api, YALPLogger logger, PlatformInfo platform) {
        this.api = api;
        this.logger = logger;
        this.platform = platform;
    }

    public YALPApi getApi() {
        return api;
    }

    public YALPLogger getLogger() {
        return logger;
    }

    public PlatformInfo getPlatform() {
        return platform;
    }
}
