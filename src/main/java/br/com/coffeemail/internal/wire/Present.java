package br.com.coffeemail.internal.wire;

import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

final class Present {

    private Present() {}

    static @Nullable String text(final @Nullable String value) {
        return value == null || value.isEmpty() ? null : value;
    }

    static <T> @Nullable List<T> list(final @Nullable List<T> value) {
        return value == null || value.isEmpty() ? null : value;
    }

    static <V> @Nullable Map<String, V> map(final @Nullable Map<String, V> value) {
        return value == null || value.isEmpty() ? null : value;
    }
}
