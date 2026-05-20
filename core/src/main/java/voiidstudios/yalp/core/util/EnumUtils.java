package voiidstudios.yalp.core.util;

import java.util.Optional;

public final class EnumUtils {
    private EnumUtils() {
    }

    public static <E extends Enum<E>> boolean contains(Class<E> type, String name) {
        return search(type, name).isPresent();
    }

    public static <E extends Enum<E>> Optional<E> search(Class<E> type, String name) {
        if (type == null || name == null) {
            return Optional.empty();
        }
        for (E value : type.getEnumConstants()) {
            if (value.name().equalsIgnoreCase(name)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public static <E extends Enum<E>> E next(E value) {
        E[] values = value.getDeclaringClass().getEnumConstants();
        return values[(value.ordinal() + 1) % values.length];
    }

    public static <E extends Enum<E>> E previous(E value) {
        E[] values = value.getDeclaringClass().getEnumConstants();
        return values[(value.ordinal() - 1 + values.length) % values.length];
    }
}
