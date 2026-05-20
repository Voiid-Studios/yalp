package voiidstudios.yalp.core.definitions;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class YamlDefinition {
    private final String id;
    private boolean enabled;
    private String name;
    private String description;
    private String type;
    private final Map<String, Object> options;
    private final Object source;

    public YamlDefinition(String id, boolean enabled, String name, String description, String type,
                          Map<String, Object> options, Object source) {
        this.id = id;
        this.enabled = enabled;
        this.name = name;
        this.description = description;
        this.type = type;
        this.options = options == null ? new LinkedHashMap<>() : new LinkedHashMap<>(options);
        this.source = source;
    }

    public String getId() { return id; }

    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public String getType() { return type; }

    public Map<String, Object> getOptions() { return Collections.unmodifiableMap(options); }

    public Object getSource() { return source; }
}
