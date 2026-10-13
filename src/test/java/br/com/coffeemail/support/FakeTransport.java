package br.com.coffeemail.support;

import br.com.coffeemail.transport.HttpRequest;
import br.com.coffeemail.transport.HttpResponse;
import br.com.coffeemail.transport.HttpTransport;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public final class FakeTransport implements HttpTransport {

    private final Function<HttpRequest, CompletableFuture<HttpResponse>> responder;
    private final List<HttpRequest> requests = new CopyOnWriteArrayList<>();
    private final List<Duration> timeouts = new CopyOnWriteArrayList<>();

    private FakeTransport(final Function<HttpRequest, CompletableFuture<HttpResponse>> responder) {
        this.responder = responder;
    }

    public static FakeTransport replying(final int status, final String body) {
        return replying(status, body, Map.of());
    }

    public static FakeTransport replying(final int status, final String body, final Map<String, String> headers) {
        return new FakeTransport(request -> CompletableFuture.completedFuture(new HttpResponse(status, headers, body)));
    }

    public static FakeTransport responding(final Function<HttpRequest, CompletableFuture<HttpResponse>> responder) {
        return new FakeTransport(responder);
    }

    public static FakeTransport failingWith(final Throwable failure) {
        return new FakeTransport(request -> CompletableFuture.failedFuture(failure));
    }

    public static FakeTransport throwingSynchronously(final RuntimeException failure) {
        return new FakeTransport(
                request -> Optional.<CompletableFuture<HttpResponse>>empty().orElseThrow(() -> failure));
    }

    @Override
    public CompletableFuture<HttpResponse> send(final HttpRequest request, final Duration timeout) {
        requests.add(request);
        timeouts.add(timeout);
        return responder.apply(request);
    }

    public List<HttpRequest> requests() {
        return List.copyOf(requests);
    }

    public HttpRequest lastRequest() {
        return requests.get(requests.size() - 1);
    }

    public Duration lastTimeout() {
        return timeouts.get(timeouts.size() - 1);
    }
}
