package voiidstudios.yalp.core.cooldown;

import java.time.Duration;
import java.util.UUID;

public interface CooldownService {
    void setCooldown(UUID id, String key, Duration duration);

    void setCooldown(String key, Duration duration);

    boolean hasCooldown(UUID id, String key);

    boolean hasCooldown(String key);

    Duration getRemaining(UUID id, String key);

    Duration getRemaining(String key);

    String formatRemaining(UUID id, String key);

    String formatRemaining(String key);

    void clearCooldown(UUID id, String key);

    void clearCooldown(String key);

    void clearAll(UUID id);

    void cleanupExpired();
}
