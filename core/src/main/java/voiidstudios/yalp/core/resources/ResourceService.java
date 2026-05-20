package voiidstudios.yalp.core.resources;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface ResourceService {
    Optional<String> readText(Object owner, String path);

    List<String> readLines(Object owner, String path);

    boolean copyResource(Object owner, String resourcePath, File destination, boolean replace);

    boolean copyDefault(Object owner, String resourcePath);

    boolean copyDefaultFolder(Object owner, String resourceFolder);

    boolean resourceExists(Object owner, String path);
}
