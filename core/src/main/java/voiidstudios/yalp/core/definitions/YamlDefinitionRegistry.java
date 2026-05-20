package voiidstudios.yalp.core.definitions;

import java.util.List;
import java.util.Optional;

public interface YamlDefinitionRegistry {
    Optional<YamlDefinition> get(String id);

    List<String> getIds();

    List<YamlDefinition> getAll();

    List<YamlDefinition> getEnabled();

    boolean setEnabled(String id, boolean enabled);

    boolean save(String id);

    void reload();
}
