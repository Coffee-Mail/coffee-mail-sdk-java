package br.com.coffeemail.internal.json;

import br.com.coffeemail.internal.result.Result;
import br.com.coffeemail.internal.result.TryCatch;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class JsonCodec {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

    private final ObjectMapper mapper;

    public JsonCodec() {
        this.mapper = JsonMapper.builder()
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .defaultPropertyInclusion(
                        JsonInclude.Value.construct(JsonInclude.Include.NON_ABSENT, JsonInclude.Include.USE_DEFAULTS))
                .build();
    }

    public Result<String, Exception> encode(final Object value) {
        return TryCatch.of(() -> mapper.writeValueAsString(value));
    }

    public <T> Result<T, Exception> decode(final String body, final ResponseType<T> type) {
        return TryCatch.of(() -> type.read(mapper, body));
    }

    public Result<JsonNode, Exception> readTree(final String body) {
        return TryCatch.of(() -> mapper.readTree(body));
    }

    public Map<String, Object> toMap(final JsonNode node) {
        if (!node.isObject()) {
            return Map.of();
        }
        return TryCatch.of(() -> mapper.convertValue(node, MAP_TYPE))
                .fold(JsonCodec::withoutNullValues, error -> Map.of());
    }

    public Result<Map<String, String>, Exception> toQuery(final Optional<Object> query) {
        return query.map(value ->
                        TryCatch.of(() -> mapper.convertValue(value, MAP_TYPE)).map(JsonCodec::toQueryValues))
                .orElseGet(() -> Result.ok(Map.of()));
    }

    private static Map<String, String> toQueryValues(final Map<String, Object> raw) {
        return Collections.unmodifiableMap(withoutNullValues(raw).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> String.valueOf(entry.getValue()),
                        (first, second) -> first,
                        LinkedHashMap::new)));
    }

    private static Map<String, Object> withoutNullValues(final Map<String, Object> raw) {
        return Collections.unmodifiableMap(raw.entrySet().stream()
                .filter(entry -> Objects.nonNull(entry.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (first, second) -> first, LinkedHashMap::new)));
    }
}
