package voiidstudios.yalp.core.api;

public final class YALPProvider {
    private static volatile YALPApi api;

    private YALPProvider() {
    }

    public static YALPApi get() {
        YALPApi current = api;
        if (current == null) {
            throw new IllegalStateException("YALP is not registered yet. Add depend: [YALP] and access it after startup.");
        }
        return current;
    }

    public static boolean isAvailable() {
        return api != null;
    }

    public static synchronized void register(YALPApi api) {
        register(api, false);
    }

    public static synchronized void register(YALPApi api, boolean replace) {
        if (api == null) {
            throw new IllegalArgumentException("YALPApi cannot be null.");
        }
        if (YALPProvider.api != null && YALPProvider.api != api && !replace) {
            throw new IllegalStateException("YALP is already registered by " + YALPProvider.api.platform().getName() + ".");
        }
        YALPProvider.api = api;
    }

    public static synchronized void unregister(YALPApi api) {
        if (YALPProvider.api == api) {
            YALPProvider.api = null;
        }
    }

    public static synchronized void unregister() {
        YALPProvider.api = null;
    }
}
