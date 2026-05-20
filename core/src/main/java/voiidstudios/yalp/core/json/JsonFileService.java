package voiidstudios.yalp.core.json;

import java.io.File;
import java.util.function.Supplier;

public interface JsonFileService {
    <T> JsonFile<T> register(Object owner, String id, File file, Class<T> type, Supplier<T> defaults);

    void saveAll();

    void reloadAll();
}
