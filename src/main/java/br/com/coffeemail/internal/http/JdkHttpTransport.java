package br.com.coffeemail.internal.http;

import br.com.coffeemail.transport.HttpRequest;
import br.com.coffeemail.transport.HttpResponse;
import br.com.coffeemail.transport.HttpTransport;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public final class JdkHttpTransport implements HttpTransport {

    private static final String HEADER_VALUE_SEPARATOR = ", ";
    private static final String PSEUDO_HEADER_PREFIX = ":";

    private final HttpClient client;

    public JdkHttpTransport(final Duration connectTimeout) {
        this.client = HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    @Override
    public CompletableFuture<HttpResponse> send(final HttpRequest request, final Duration timeout) {
        final java.net.http.HttpRequest.Builder builder = java.net.http.HttpRequest.newBuilder(request.uri())
                .timeout(timeout)
                .method(
                        request.method().name(),
                        request.body().map(BodyPublishers::ofString).orElseGet(BodyPublishers::noBody));
        request.headers().forEach(builder::header);
        return client.sendAsync(builder.build(), BodyHandlers.ofString())
                .thenApply(response ->
                        new HttpResponse(response.statusCode(), flatten(response.headers()), response.body()));
    }

    private static Map<String, String> flatten(final HttpHeaders headers) {
        return headers.map().entrySet().stream()
                .filter(entry -> !entry.getKey().startsWith(PSEUDO_HEADER_PREFIX))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> String.join(HEADER_VALUE_SEPARATOR, entry.getValue()),
                        (first, second) -> first));
    }
}
