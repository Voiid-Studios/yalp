package voiidstudios.yalp.core.translation;

import java.io.File;
import java.util.Map;

public interface TranslationService {
    String get(String key);

    String get(String key, Map<String, String> placeholders);

    String get(String key, String... placeholders);

    java.util.List<String> getList(String key);

    TranslationContext createContext(String pluginName, File dataFolder, ClassLoader classLoader,
                                     String resourceFolder, String baseLanguage, String selectedLanguage);

    void reload();
}
