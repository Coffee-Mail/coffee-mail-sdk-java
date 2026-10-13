package br.com.coffeemail.support;

import br.com.coffeemail.CoffeeMailLocale;
import br.com.coffeemail.internal.ClientConfig;
import br.com.coffeemail.internal.errors.ErrorMapper;
import br.com.coffeemail.internal.http.RequestExecutor;
import br.com.coffeemail.internal.json.JsonCodec;
import br.com.coffeemail.transport.HttpTransport;
import java.net.URI;
import java.time.Clock;
import java.time.Duration;

public final class TestClients {

    public static final String API_KEY = "cm_live_test";
    public static final URI BASE_URL = URI.create("https://api.test.local");
    public static final Duration TIMEOUT = Duration.ofSeconds(10);

    private TestClients() {}

    public static ClientConfig config(final HttpTransport transport) {
        return config(transport, CoffeeMailLocale.PT_BR);
    }

    public static ClientConfig config(final HttpTransport transport, final CoffeeMailLocale locale) {
        return new ClientConfig(API_KEY, BASE_URL, locale, TIMEOUT, transport, Clock.systemUTC());
    }

    public static RequestExecutor executor(final HttpTransport transport) {
        return executor(config(transport));
    }

    public static RequestExecutor executor(final ClientConfig config) {
        final JsonCodec codec = new JsonCodec();
        return new RequestExecutor(config, codec, new ErrorMapper(codec));
    }
}
