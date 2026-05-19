package voiidstudios.yalp.core.config;

import java.util.Optional;

public interface ConfigService {
    Optional<Object> load(String owner, String fileName);

    void save(String owner, String fileName);

    Optional<Object> reload(String owner, String fileName);
}
