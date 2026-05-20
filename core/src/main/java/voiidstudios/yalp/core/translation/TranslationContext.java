package voiidstudios.yalp.core.translation;

import java.util.List;
import java.util.Map;

public interface TranslationContext {
    String getName();

    String getLanguage();

    String get(String key);

    String get(String key, Map<String, String> placeholders);

    String get(String key, String... placeholders);

    List<String> getList(String key);

    List<String> getList(String key, Map<String, String> placeholders);

    void reload();
}
