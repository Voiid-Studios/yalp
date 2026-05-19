package dev.maxi.yalp.api;

public final class YALPProvider {
    private static volatile YALPApi api;

    private YALPProvider() {
    }

    public static YALPApi get() {
        YALPApi current = api;
        if (current == null) {
            throw new IllegalStateException("YALP is not available yet. Add depend: [YALP] and access it after plugin startup.");
        }
        return current;
    }

    public static boolean isAvailable() {
        return api != null;
    }

    public static void set(YALPApi api) {
        YALPProvider.api = api;
    }

    public static void unset(YALPApi api) {
        if (YALPProvider.api == api) {
            YALPProvider.api = null;
        }
    }
}
