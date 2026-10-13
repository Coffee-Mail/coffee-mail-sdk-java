package br.com.coffeemail.internal.http;

import br.com.coffeemail.errors.CoffeeMailException;
import br.com.coffeemail.internal.json.ResponseType;
import br.com.coffeemail.transport.HttpMethod;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

public record Operation<T>(
        HttpMethod method,
        String path,
        Optional<Object> query,
        Optional<Object> body,
        Map<String, String> headers,
        ResponseType<T> responseType,
        UnaryOperator<CoffeeMailException> errorMapping) {

    public Operation {
        headers = Map.copyOf(headers);
    }

    public static <T> Operation<T> get(final String path, final ResponseType<T> responseType) {
        return of(HttpMethod.GET, path, responseType);
    }

    public static <T> Operation<T> post(final String path, final ResponseType<T> responseType) {
        return of(HttpMethod.POST, path, responseType);
    }

    public static <T> Operation<T> put(final String path, final ResponseType<T> responseType) {
        return of(HttpMethod.PUT, path, responseType);
    }

    public static <T> Operation<T> patch(final String path, final ResponseType<T> responseType) {
        return of(HttpMethod.PATCH, path, responseType);
    }

    public static <T> Operation<T> delete(final String path, final ResponseType<T> responseType) {
        return of(HttpMethod.DELETE, path, responseType);
    }

    private static <T> Operation<T> of(final HttpMethod method, final String path, final ResponseType<T> type) {
        return new Operation<>(
                method, path, Optional.empty(), Optional.empty(), Map.of(), type, UnaryOperator.identity());
    }

    public Operation<T> withQuery(final Object value) {
        return new Operation<>(method, path, Optional.of(value), body, headers, responseType, errorMapping);
    }

    public Operation<T> withBody(final Object value) {
        return new Operation<>(method, path, query, Optional.of(value), headers, responseType, errorMapping);
    }

    public Operation<T> withHeaders(final Map<String, String> value) {
        return new Operation<>(method, path, query, body, value, responseType, errorMapping);
    }

    public Operation<T> withErrorMapping(final UnaryOperator<CoffeeMailException> value) {
        return new Operation<>(method, path, query, body, headers, responseType, value);
    }
}
