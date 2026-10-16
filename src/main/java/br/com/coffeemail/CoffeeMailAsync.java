package br.com.coffeemail;

import br.com.coffeemail.internal.ClientConfig;
import br.com.coffeemail.internal.ClientRuntime;
import br.com.coffeemail.internal.constants.SdkVersion;
import br.com.coffeemail.internal.resources.DefaultAudiencesAsync;
import br.com.coffeemail.internal.resources.DefaultBroadcastsAsync;
import br.com.coffeemail.internal.resources.DefaultDomainsAsync;
import br.com.coffeemail.internal.resources.DefaultEmailsAsync;
import br.com.coffeemail.internal.resources.DefaultSendersAsync;
import br.com.coffeemail.internal.resources.DefaultStatsAsync;
import br.com.coffeemail.internal.resources.DefaultSuppressionsAsync;
import br.com.coffeemail.internal.resources.DefaultTemplatesAsync;
import br.com.coffeemail.internal.resources.DefaultValidationAsync;
import br.com.coffeemail.internal.resources.DefaultWebhooksAsync;
import br.com.coffeemail.models.common.ApiKeyIntrospection;
import br.com.coffeemail.resources.AudiencesAsync;
import br.com.coffeemail.resources.BroadcastsAsync;
import br.com.coffeemail.resources.DomainsAsync;
import br.com.coffeemail.resources.EmailsAsync;
import br.com.coffeemail.resources.SendersAsync;
import br.com.coffeemail.resources.StatsAsync;
import br.com.coffeemail.resources.SuppressionsAsync;
import br.com.coffeemail.resources.TemplatesAsync;
import br.com.coffeemail.resources.ValidationAsync;
import br.com.coffeemail.resources.WebhooksAsync;
import java.util.concurrent.CompletableFuture;

/**
 * Cliente assíncrono oficial da CoffeeMail: os mesmos recursos de {@link CoffeeMail}, retornando
 * {@link CompletableFuture}. Os futuros nunca completam com exceção por erro de API: o erro vem no envelope.
 */
public final class CoffeeMailAsync {

    private final ClientRuntime runtime;
    private final EmailsAsync emails;
    private final DomainsAsync domains;
    private final TemplatesAsync templates;
    private final AudiencesAsync audiences;
    private final BroadcastsAsync broadcasts;
    private final SendersAsync senders;
    private final SuppressionsAsync suppressions;
    private final WebhooksAsync webhooks;
    private final ValidationAsync validation;
    private final StatsAsync stats;

    private CoffeeMailAsync(final ClientConfig config) {
        this.runtime = ClientRuntime.create(config);
        this.emails = new DefaultEmailsAsync(runtime);
        this.domains = new DefaultDomainsAsync(runtime);
        this.templates = new DefaultTemplatesAsync(runtime);
        this.audiences = new DefaultAudiencesAsync(runtime);
        this.broadcasts = new DefaultBroadcastsAsync(runtime);
        this.senders = new DefaultSendersAsync(runtime);
        this.suppressions = new DefaultSuppressionsAsync(runtime);
        this.webhooks = new DefaultWebhooksAsync(runtime);
        this.stats = new DefaultStatsAsync(runtime);
        this.validation = new DefaultValidationAsync(runtime);
    }

    /**
     * Inicia a configuração de um cliente.
     *
     * @return o builder
     */
    public static CoffeeMailBuilder<CoffeeMailAsync> builder() {
        return new CoffeeMailBuilder<>(CoffeeMailAsync::new);
    }

    /**
     * Versão do SDK, a mesma enviada no cabeçalho {@code User-Agent}.
     *
     * @return a versão, como {@code 0.1.0}
     */
    public static String version() {
        return SdkVersion.VALUE;
    }

    /**
     * Cria um cliente com a chave de {@code COFFEEMAIL_API_KEY} e as configurações padrão.
     *
     * @return o cliente
     */
    public static CoffeeMailAsync create() {
        return builder().build();
    }

    /**
     * Envio e consulta de e-mails transacionais.
     *
     * @return o recurso
     */
    public EmailsAsync emails() {
        return emails;
    }

    /**
     * Cadastro e verificação de domínios de envio.
     *
     * @return o recurso
     */
    public DomainsAsync domains() {
        return domains;
    }

    /**
     * Gestão e renderização de templates.
     *
     * @return o recurso
     */
    public TemplatesAsync templates() {
        return templates;
    }

    /**
     * Audiências e contatos.
     *
     * @return o recurso
     */
    public AudiencesAsync audiences() {
        return audiences;
    }

    /**
     * Campanhas enviadas a audiências.
     *
     * @return o recurso
     */
    public BroadcastsAsync broadcasts() {
        return broadcasts;
    }

    /**
     * Remetentes verificados por endereço.
     *
     * @return o recurso
     */
    public SendersAsync senders() {
        return senders;
    }

    /**
     * Lista de supressão.
     *
     * @return o recurso
     */
    public SuppressionsAsync suppressions() {
        return suppressions;
    }

    /**
     * Endpoints de webhook e verificação de assinatura.
     *
     * @return o recurso
     */
    public WebhooksAsync webhooks() {
        return webhooks;
    }

    /**
     * Estatísticas de entregabilidade.
     *
     * @return o recurso
     */
    public StatsAsync stats() {
        return stats;
    }

    /**
     * Validação de endereços de e-mail antes do envio.
     *
     * @return o recurso
     */
    public ValidationAsync validation() {
        return validation;
    }

    /**
     * Consulta os metadados da chave de API em uso, com cache de 5 minutos.
     *
     * @return os metadados da chave
     */
    public CompletableFuture<CoffeeMailResponse<ApiKeyIntrospection>> introspect() {
        return introspect(false);
    }

    /**
     * Consulta os metadados da chave de API em uso.
     *
     * @param forceRefresh ignora o cache quando {@code true}
     * @return os metadados da chave
     */
    public CompletableFuture<CoffeeMailResponse<ApiKeyIntrospection>> introspect(final boolean forceRefresh) {
        return runtime.introspection().introspect(forceRefresh);
    }

    /** Descarta o cache da introspecção da chave de API. */
    public void invalidateApiKeyCache() {
        runtime.introspection().invalidate();
    }
}
