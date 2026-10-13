package br.com.coffeemail;

import br.com.coffeemail.errors.ApiErrorDetails;
import br.com.coffeemail.errors.AuthenticationException;
import br.com.coffeemail.internal.ClientConfig;
import br.com.coffeemail.internal.constants.Defaults;
import br.com.coffeemail.internal.http.JdkHttpTransport;
import br.com.coffeemail.internal.i18n.MessageKey;
import br.com.coffeemail.internal.i18n.Messages;
import br.com.coffeemail.transport.HttpTransport;
import java.net.URI;
import java.time.Clock;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;

/**
 * Configuração de um cliente CoffeeMail.
 *
 * @param <C> tipo do cliente construído
 */
public final class CoffeeMailBuilder<C> {

    private static final String TRAILING_SLASH = "/";

    private final Function<ClientConfig, C> factory;
    private @Nullable String apiKey;
    private URI baseUrl = Defaults.BASE_URL;
    private CoffeeMailLocale locale = CoffeeMailLocale.PT_BR;
    private Duration timeout = Defaults.TIMEOUT;
    private @Nullable HttpTransport transport;
    private Clock clock = Clock.systemUTC();
    private Function<String, Optional<String>> environment = name -> Optional.ofNullable(System.getenv(name));

    CoffeeMailBuilder(final Function<ClientConfig, C> factory) {
        this.factory = factory;
    }

    /**
     * Chave de API de produto. Quando omitida, é lida de {@code COFFEEMAIL_API_KEY}.
     *
     * @param value a chave, como {@code cm_live_...}
     * @return este builder
     */
    public CoffeeMailBuilder<C> apiKey(final String value) {
        this.apiKey = value;
        return this;
    }

    /**
     * URL base da API. Padrão: {@code https://api.coffeemail.com.br}.
     *
     * @param value a URL base
     * @return este builder
     */
    public CoffeeMailBuilder<C> baseUrl(final String value) {
        return baseUrl(URI.create(value));
    }

    /**
     * URL base da API. Padrão: {@code https://api.coffeemail.com.br}.
     *
     * @param value a URL base
     * @return este builder
     */
    public CoffeeMailBuilder<C> baseUrl(final URI value) {
        this.baseUrl = value;
        return this;
    }

    /**
     * Idioma das mensagens de erro. Padrão: {@link CoffeeMailLocale#PT_BR}.
     *
     * @param value o idioma
     * @return este builder
     */
    public CoffeeMailBuilder<C> locale(final CoffeeMailLocale value) {
        this.locale = value;
        return this;
    }

    /**
     * Tempo limite de cada requisição. Padrão: 10 segundos.
     *
     * @param value o tempo limite
     * @return este builder
     */
    public CoffeeMailBuilder<C> timeout(final Duration value) {
        this.timeout = value;
        return this;
    }

    /**
     * Transporte HTTP customizado, para proxies ou testes. Padrão: {@code java.net.http.HttpClient}.
     *
     * @param value o transporte
     * @return este builder
     */
    public CoffeeMailBuilder<C> transport(final HttpTransport value) {
        this.transport = value;
        return this;
    }

    CoffeeMailBuilder<C> clock(final Clock value) {
        this.clock = value;
        return this;
    }

    CoffeeMailBuilder<C> environment(final Function<String, Optional<String>> value) {
        this.environment = value;
        return this;
    }

    /**
     * Constrói o cliente.
     *
     * @return o cliente configurado
     * @throws AuthenticationException quando nenhuma chave de API for informada
     */
    public C build() {
        final String explicitKey = apiKey;
        final Optional<String> candidateKey = explicitKey == null
                ? environment.apply(Defaults.API_KEY_ENVIRONMENT_VARIABLE)
                : Optional.of(explicitKey);
        final HttpTransport customTransport = transport;
        final String resolvedKey = candidateKey
                .map(String::strip)
                .filter(key -> !key.isEmpty())
                .orElseThrow(() -> new AuthenticationException(
                        ApiErrorDetails.of(Messages.format(locale, MessageKey.MISSING_API_KEY))));
        return factory.apply(new ClientConfig(
                resolvedKey,
                withoutTrailingSlash(baseUrl),
                locale,
                timeout,
                customTransport == null ? new JdkHttpTransport(timeout) : customTransport,
                clock));
    }

    private static URI withoutTrailingSlash(final URI value) {
        final String text = value.toString();
        return text.endsWith(TRAILING_SLASH) ? URI.create(text.substring(0, text.length() - 1)) : value;
    }
}
