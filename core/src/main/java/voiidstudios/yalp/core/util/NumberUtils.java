package voiidstudios.yalp.core.util;

public final class NumberUtils {
    private NumberUtils() {
    }

    public static boolean isInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static int toInt(String value, int fallback) {
        return isInt(value) ? Integer.parseInt(value) : fallback;
    }

    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static double toDouble(String value, double fallback) {
        return isDouble(value) ? Double.parseDouble(value) : fallback;
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static boolean inRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
}
