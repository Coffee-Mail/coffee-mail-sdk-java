package br.com.coffeemail.internal.json;

import br.com.coffeemail.NoContent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public final class ResponseType<T> {

    private final BodyReader<T> reader;
    private final Optional<T> emptyValue;

    private ResponseType(final BodyReader<T> reader, final Optional<T> emptyValue) {
        this.reader = reader;
        this.emptyValue = emptyValue;
    }

    public static <T> ResponseType<T> of(final Class<T> type) {
        return new ResponseType<>((mapper, body) -> mapper.readValue(body, type), Optional.empty());
    }

    public static <E> ResponseType<List<E>> listOf(final Class<E> elementType) {
        return new ResponseType<>(
                (mapper, body) -> mapper.readValue(
                        body, mapper.getTypeFactory().constructCollectionType(List.class, elementType)),
                Optional.empty());
    }

    public static <T> ResponseType<Optional<T>> optionalOf(final Class<T> type) {
        return new ResponseType<>((mapper, body) -> readOptional(mapper, body, type), Optional.of(Optional.empty()));
    }

    public static ResponseType<NoContent> noContent() {
        return new ResponseType<>((mapper, body) -> NoContent.INSTANCE, Optional.of(NoContent.INSTANCE));
    }

    private static <T> Optional<T> readOptional(final ObjectMapper mapper, final String body, final Class<T> type)
            throws IOException {
        final JsonNode node = mapper.readTree(body);
        if (node.isNull() || node.isMissingNode()) {
            return Optional.empty();
        }
        return Optional.of(mapper.treeToValue(node, type));
    }

    T read(final ObjectMapper mapper, final String body) throws IOException {
        return reader.read(mapper, body);
    }

    public Optional<T> emptyValue() {
        return emptyValue;
    }

    @FunctionalInterface
    private interface BodyReader<T> {
        T read(ObjectMapper mapper, String body) throws IOException;
    }
}
