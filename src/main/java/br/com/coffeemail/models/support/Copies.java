package br.com.coffeemail.models.support;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public final class Copies {

    private Copies() {}

    public static <T> List<T> list(final @Nullable List<T> value) {
        return value == null ? List.of() : List.copyOf(value);
    }

    public static <T> Optional<T> optional(final Optional<T> value) {
        return Objects.requireNonNullElse(value, Optional.empty());
    }

    public static <V> Map<String, V> map(final @Nullable Map<String, V> value) {
        return value == null ? Map.of() : Collections.unmodifiableMap(new LinkedHashMap<>(value));
    }

    public static <T> @Nullable List<T> nullableList(final @Nullable List<T> value) {
        return value == null ? null : List.copyOf(value);
    }

    public static <V> @Nullable Map<String, V> nullableMap(final @Nullable Map<String, V> value) {
        return value == null ? null : Collections.unmodifiableMap(new LinkedHashMap<>(value));
    }
}
