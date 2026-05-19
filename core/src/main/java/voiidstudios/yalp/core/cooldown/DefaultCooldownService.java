package voiidstudios.yalp.core.cooldown;

import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class DefaultCooldownService implements CooldownService {
    private static final UUID GLOBAL_ID = new UUID(0L, 0L);
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    @Override
    public void setCooldown(UUID id, String key, Duration duration) {
        if (id == null) {
            throw new IllegalArgumentException("UUID cannot be null.");
        }
        long expiresAt = System.currentTimeMillis() + Math.max(0L, duration.toMillis());
        cooldowns.computeIfAbsent(id, ignored -> new HashMap<>()).put(normalize(key), expiresAt);
    }

    @Override
    public void setCooldown(String key, Duration duration) {
        setCooldown(GLOBAL_ID, key, duration);
    }

    @Override
    public boolean hasCooldown(UUID id, String key) {
        return getRemaining(id, key).toMillis() > 0L;
    }

    @Override
    public boolean hasCooldown(String key) {
        return hasCooldown(GLOBAL_ID, key);
    }

    @Override
    public Duration getRemaining(UUID id, String key) {
        Map<String, Long> userCooldowns = cooldowns.get(id);
        if (userCooldowns == null) {
            return Duration.ZERO;
        }
        Long expiresAt = userCooldowns.get(normalize(key));
        if (expiresAt == null) {
            return Duration.ZERO;
        }
        long remaining = expiresAt - System.currentTimeMillis();
        if (remaining <= 0L) {
            clearCooldown(id, key);
            return Duration.ZERO;
        }
        return Duration.ofMillis(remaining);
    }

    @Override
    public Duration getRemaining(String key) {
        return getRemaining(GLOBAL_ID, key);
    }

    @Override
    public String formatRemaining(UUID id, String key) {
        return format(getRemaining(id, key));
    }

    @Override
    public String formatRemaining(String key) {
        return format(getRemaining(key));
    }

    @Override
    public void clearCooldown(UUID id, String key) {
        Map<String, Long> userCooldowns = cooldowns.get(id);
        if (userCooldowns == null) {
            return;
        }
        userCooldowns.remove(normalize(key));
        if (userCooldowns.isEmpty()) {
            cooldowns.remove(id);
        }
    }

    @Override
    public void clearCooldown(String key) {
        clearCooldown(GLOBAL_ID, key);
    }

    @Override
    public void clearAll(UUID id) {
        cooldowns.remove(id);
    }

    @Override
    public void cleanupExpired() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<UUID, Map<String, Long>>> users = cooldowns.entrySet().iterator();
        while (users.hasNext()) {
            Map<String, Long> values = users.next().getValue();
            values.entrySet().removeIf(entry -> entry.getValue() <= now);
            if (values.isEmpty()) {
                users.remove();
            }
        }
    }

    private String format(Duration duration) {
        long seconds = Math.max(0L, duration.getSeconds());
        long days = seconds / 86400L;
        seconds %= 86400L;
        long hours = seconds / 3600L;
        seconds %= 3600L;
        long minutes = seconds / 60L;
        seconds %= 60L;
        if (days > 0L) {
            return days + "d " + hours + "h";
        }
        if (hours > 0L) {
            return hours + "h " + minutes + "m";
        }
        if (minutes > 0L) {
            return minutes + "m " + seconds + "s";
        }
        return seconds + "s";
    }

    private String normalize(String key) {
        return key == null ? "default" : key.trim().toLowerCase(Locale.ROOT);
    }
}
