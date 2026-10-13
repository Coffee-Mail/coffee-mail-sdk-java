package br.com.coffeemail.support;

import br.com.coffeemail.internal.result.TryCatch;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class StubHttpServer implements AutoCloseable {

    private static final int BACKLOG = 16;
    private static final int CARRIAGE_RETURN = '\r';
    private static final int LINE_FEED = '\n';
    private static final int END_OF_STREAM = -1;
    private static final String CONTENT_LENGTH = "content-length";
    private static final String HEADER_SEPARATOR = ":";
    private static final String CRLF = "\r\n";

    public record RecordedRequest(String method, String path, Map<String, String> headers, String body) {}

    public record StubResponse(int status, Map<String, List<String>> headers, String body) {}

    private final ServerSocket server;
    private final Function<RecordedRequest, Optional<StubResponse>> handler;
    private final ExecutorService workers = Executors.newCachedThreadPool();
    private final List<RecordedRequest> requests = new CopyOnWriteArrayList<>();
    private final List<Socket> clients = new CopyOnWriteArrayList<>();

    private StubHttpServer(final ServerSocket server, final Function<RecordedRequest, Optional<StubResponse>> handler) {
        this.server = server;
        this.handler = handler;
    }

    public static StubHttpServer start(final Function<RecordedRequest, Optional<StubResponse>> handler)
            throws IOException {
        final StubHttpServer stub =
                new StubHttpServer(new ServerSocket(0, BACKLOG, InetAddress.getLoopbackAddress()), handler);
        stub.workers.execute(stub::acceptConnections);
        return stub;
    }

    public URI uri(final String path) {
        return URI.create("http://" + server.getInetAddress().getHostAddress() + ":" + server.getLocalPort() + path);
    }

    public List<RecordedRequest> requests() {
        return List.copyOf(requests);
    }

    @Override
    public void close() throws IOException {
        workers.shutdownNow();
        clients.forEach(client -> TryCatch.of(() -> {
            client.close();
            return client;
        }));
        server.close();
    }

    private void acceptConnections() {
        while (!server.isClosed()) {
            TryCatch.of(server::accept).map(client -> {
                clients.add(client);
                workers.execute(() -> TryCatch.of(() -> serve(client)));
                return client;
            });
        }
    }

    private Socket serve(final Socket client) throws IOException {
        final InputStream input = client.getInputStream();
        final String[] requestLine = readLine(input).split(" ", 3);
        final Map<String, String> headers = Stream.generate(
                        () -> TryCatch.of(() -> readLine(input)).fold(line -> line, failure -> ""))
                .takeWhile(line -> !line.isEmpty())
                .map(line -> line.split(HEADER_SEPARATOR, 2))
                .collect(Collectors.toMap(
                        pair -> pair[0].strip().toLowerCase(Locale.ROOT),
                        pair -> pair[1].strip(),
                        (first, second) -> first + ", " + second));
        final int length = Integer.parseInt(headers.getOrDefault(CONTENT_LENGTH, "0"));
        final String body = new String(input.readNBytes(length), StandardCharsets.UTF_8);
        final RecordedRequest request = new RecordedRequest(requestLine[0], requestLine[1], headers, body);
        requests.add(request);
        final Optional<StubResponse> response = handler.apply(request);
        if (response.isPresent()) {
            write(client.getOutputStream(), response.get());
            client.close();
        }
        return client;
    }

    private static void write(final OutputStream output, final StubResponse response) throws IOException {
        final byte[] body = response.body().getBytes(StandardCharsets.UTF_8);
        final String extraHeaders = response.headers().entrySet().stream()
                .flatMap(header -> header.getValue().stream().map(value -> header.getKey() + ": " + value + CRLF))
                .collect(Collectors.joining());
        final String head = "HTTP/1.1 " + response.status() + " Stub" + CRLF
                + "Content-Length: " + body.length + CRLF
                + "Connection: close" + CRLF
                + extraHeaders + CRLF;
        output.write(head.getBytes(StandardCharsets.ISO_8859_1));
        output.write(body);
        output.flush();
    }

    private static String readLine(final InputStream input) throws IOException {
        final ByteArrayOutputStream line = new ByteArrayOutputStream();
        for (int current = input.read(); current != LINE_FEED && current != END_OF_STREAM; current = input.read()) {
            line.write(current);
        }
        final String text = line.toString(StandardCharsets.ISO_8859_1);
        return text.endsWith(String.valueOf((char) CARRIAGE_RETURN)) ? text.substring(0, text.length() - 1) : text;
    }
}
