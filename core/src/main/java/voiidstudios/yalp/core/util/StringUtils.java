package voiidstudios.yalp.core.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class StringUtils {
    private StringUtils() {
    }

    public static String replacePlaceholders(String text, Map<String, String> placeholders) {
        String output = text == null ? "" : text;
        if (placeholders == null) {
            return output;
        }
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            output = output.replace("{" + entry.getKey() + "}", entry.getValue() == null ? "" : entry.getValue());
        }
        return output;
    }

    public static List<String> splitLines(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(text.split("\\r?\\n"));
    }

    public static String repeat(String text, int times) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < Math.max(0, times); i++) {
            builder.append(text);
        }
        return builder.toString();
    }
}
