package br.com.coffeemail.internal;

import br.com.coffeemail.internal.errors.ErrorMapper;
import br.com.coffeemail.internal.http.RequestExecutor;
import br.com.coffeemail.internal.introspection.IntrospectionCache;
import br.com.coffeemail.internal.json.JsonCodec;

public final class ClientRuntime {

    private final ClientConfig config;
    private final RequestExecutor executor;
    private final IntrospectionCache introspection;

    private ClientRuntime(
            final ClientConfig config, final RequestExecutor executor, final IntrospectionCache introspection) {
        this.config = config;
        this.executor = executor;
        this.introspection = introspection;
    }

    public static ClientRuntime create(final ClientConfig config) {
        final JsonCodec codec = new JsonCodec();
        final RequestExecutor executor = new RequestExecutor(config, codec, new ErrorMapper(codec));
        return new ClientRuntime(config, executor, new IntrospectionCache(executor, config.clock()));
    }

    public ClientConfig config() {
        return config;
    }

    public RequestExecutor executor() {
        return executor;
    }

    public IntrospectionCache introspection() {
        return introspection;
    }
}
