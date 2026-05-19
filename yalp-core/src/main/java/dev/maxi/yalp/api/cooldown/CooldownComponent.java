package dev.maxi.yalp.api.cooldown;

import dev.maxi.yalp.api.component.YALPComponent;

import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public final class CooldownComponent implements YALPComponent {
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    @Override
    public String getId() {
        return "cooldown";
    }

    public void setCooldown(UUID id, String key, Duration duration) {
        if (id == null) {
            throw new IllegalArgumentException("UUID cannot be null.");
        }
        long expiresAt = System.currentTimeMillis() + Math.max(0L, duration.toMillis());
        cooldowns.computeIfAbsent(id, ignored -> new HashMap<>()).put(normalize(key), expiresAt);
    }

    public boolean hasCooldown(UUID id, String key) {
        return getRemaining(id, key).toMillis() > 0L;
    }

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

    public void clearAll(UUID id) {
        cooldowns.remove(id);
    }

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

    private String normalize(String key) {
        return key == null ? "default" : key.trim().toLowerCase(Locale.ROOT);
    }
}
