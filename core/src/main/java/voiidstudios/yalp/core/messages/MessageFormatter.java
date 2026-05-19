package voiidstudios.yalp.core.messages;

import java.util.List;
import java.util.Map;

public interface MessageFormatter {
    String color(String text);

    List<String> color(List<String> lines);

    String format(String message, Map<String, String> placeholders);

    String format(String message, String... placeholders);

    String stripColors(String text);

    String center(String message);

    String getPrefix();

    void setPrefix(String prefix);
}
