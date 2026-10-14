package br.com.coffeemail.support;

import br.com.coffeemail.internal.result.TryCatch;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public final class Fixtures {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JsonNode CONTRACT = read(Path.of("src/test/resources/contract/fixtures.json"));
    private static final JsonNode SPEC = read(Path.of("spec/product.json"));

    private Fixtures() {}

    public record Fixture(int status, String body) {}

    private static JsonNode read(final Path path) {
        return TryCatch.of(() -> MAPPER.readTree(Files.readString(path)))
                .fold(node -> node, error -> MissingNode.getInstance());
    }

    public static String id(final String name) {
        return CONTRACT.path("ids").path(name).asText();
    }

    public static Fixture response(final String name) {
        final JsonNode fixture = CONTRACT.path("fixtures").path(name);
        final JsonNode body = fixture.path("body");
        return new Fixture(
                fixture.path("status").asInt(), body.isNull() || body.isMissingNode() ? "" : body.toString());
    }

    public static String body(final String name) {
        return response(name).body();
    }

    public static List<String> specOperations() {
        return SPEC.path("paths").properties().stream()
                .flatMap(path -> Stream.of("get", "post", "put", "patch", "delete")
                        .filter(method -> path.getValue().has(method))
                        .map(method -> method.toUpperCase(Locale.ROOT) + " " + path.getKey()))
                .toList();
    }
}
