package voiidstudios.yalp.core.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicMessageFormatter implements MessageFormatter {
    private static final int CENTER_PX = 154;
    private String prefix = "&7[&aYALP&7]&r";

    @Override
    public String color(String text) {
        return text == null ? "" : text.replace('&', '\u00a7');
    }

    @Override
    public List<String> color(List<String> lines) {
        List<String> colored = new ArrayList<>();
        if (lines == null) {
            return colored;
        }
        for (String line : lines) {
            colored.add(color(line));
        }
        return colored;
    }

    @Override
    public String format(String message, Map<String, String> placeholders) {
        String output = message == null ? "" : message;
        Map<String, String> merged = new HashMap<>();
        merged.put("prefix", prefix);
        if (placeholders != null) {
            merged.putAll(placeholders);
        }
        for (Map.Entry<String, String> entry : merged.entrySet()) {
            output = output.replace("{" + entry.getKey() + "}", entry.getValue() == null ? "" : entry.getValue());
        }
        return color(output);
    }

    @Override
    public String format(String message, String... placeholders) {
        Map<String, String> mapped = new HashMap<>();
        if (placeholders != null) {
            for (int i = 0; i + 1 < placeholders.length; i += 2) {
                mapped.put(placeholders[i], placeholders[i + 1]);
            }
        }
        return format(message, mapped);
    }

    @Override
    public String stripColors(String text) {
        return color(text).replaceAll("(?i)\u00a7[0-9A-FK-ORX]", "");
    }

    @Override
    public String center(String message) {
        String colored = color(message);
        String stripped = stripColors(colored);
        int messagePxSize = stripped.length() * 6;
        int toCompensate = CENTER_PX - (messagePxSize / 2);
        StringBuilder builder = new StringBuilder();
        while (toCompensate > 4) {
            builder.append(' ');
            toCompensate -= 4;
        }
        return builder.append(colored).toString();
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix == null ? "" : prefix;
    }
}
