package br.com.coffeemail;

import br.com.coffeemail.internal.ClientConfig;
import br.com.coffeemail.internal.ClientRuntime;
import br.com.coffeemail.internal.constants.SdkVersion;
import br.com.coffeemail.internal.resources.DefaultAudiences;
import br.com.coffeemail.internal.resources.DefaultAudiencesAsync;
import br.com.coffeemail.internal.resources.DefaultBroadcasts;
import br.com.coffeemail.internal.resources.DefaultBroadcastsAsync;
import br.com.coffeemail.internal.resources.DefaultDomains;
import br.com.coffeemail.internal.resources.DefaultDomainsAsync;
import br.com.coffeemail.internal.resources.DefaultEmails;
import br.com.coffeemail.internal.resources.DefaultEmailsAsync;
import br.com.coffeemail.internal.resources.DefaultSenders;
import br.com.coffeemail.internal.resources.DefaultSendersAsync;
import br.com.coffeemail.internal.resources.DefaultStats;
import br.com.coffeemail.internal.resources.DefaultStatsAsync;
import br.com.coffeemail.internal.resources.DefaultSuppressions;
import br.com.coffeemail.internal.resources.DefaultSuppressionsAsync;
import br.com.coffeemail.internal.resources.DefaultTemplates;
import br.com.coffeemail.internal.resources.DefaultTemplatesAsync;
import br.com.coffeemail.internal.resources.DefaultWebhooks;
import br.com.coffeemail.internal.resources.DefaultWebhooksAsync;
import br.com.coffeemail.models.common.ApiKeyIntrospection;
import br.com.coffeemail.resources.Audiences;
import br.com.coffeemail.resources.Broadcasts;
import br.com.coffeemail.resources.Domains;
import br.com.coffeemail.resources.Emails;
import br.com.coffeemail.resources.Senders;
import br.com.coffeemail.resources.Stats;
import br.com.coffeemail.resources.Suppressions;
import br.com.coffeemail.resources.Templates;
import br.com.coffeemail.resources.Webhooks;

/**
 * Cliente síncrono oficial da CoffeeMail. É thread-safe: crie uma instância e reutilize-a.
 *
 * <pre>{@code
 * CoffeeMail coffeemail = CoffeeMail.builder().apiKey("cm_live_...").build();
 * CoffeeMailResponse<SendEmailResponse> response = coffeemail.emails().send(
 *         SendEmailInput.builder("contato@empresa.com.br", "cliente@gmail.com")
 *                 .subject("Pedido confirmado")
 *                 .html("<h1>Obrigado!</h1>")
 *                 .build());
 * }</pre>
 */
public final class CoffeeMail {

    private final ClientRuntime runtime;
    private final Emails emails;
    private final Domains domains;
    private final Templates templates;
    private final Audiences audiences;
    private final Broadcasts broadcasts;
    private final Senders senders;
    private final Suppressions suppressions;
    private final Webhooks webhooks;
    private final Stats stats;

    private CoffeeMail(final ClientConfig config) {
        this.runtime = ClientRuntime.create(config);
        this.emails = new DefaultEmails(new DefaultEmailsAsync(runtime));
        this.domains = new DefaultDomains(new DefaultDomainsAsync(runtime));
        this.templates = new DefaultTemplates(new DefaultTemplatesAsync(runtime));
        this.audiences = new DefaultAudiences(new DefaultAudiencesAsync(runtime));
        this.broadcasts = new DefaultBroadcasts(new DefaultBroadcastsAsync(runtime));
        this.senders = new DefaultSenders(new DefaultSendersAsync(runtime));
        this.suppressions = new DefaultSuppressions(new DefaultSuppressionsAsync(runtime));
        this.webhooks = new DefaultWebhooks(new DefaultWebhooksAsync(runtime));
        this.stats = new DefaultStats(new DefaultStatsAsync(runtime));
    }

    /**
     * Inicia a configuração de um cliente.
     *
     * @return o builder
     */
    public static CoffeeMailBuilder<CoffeeMail> builder() {
        return new CoffeeMailBuilder<>(CoffeeMail::new);
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
    public static CoffeeMail create() {
        return builder().build();
    }

    /**
     * Envio e consulta de e-mails transacionais.
     *
     * @return o recurso
     */
    public Emails emails() {
        return emails;
    }

    /**
     * Cadastro e verificação de domínios de envio.
     *
     * @return o recurso
     */
    public Domains domains() {
        return domains;
    }

    /**
     * Gestão e renderização de templates.
     *
     * @return o recurso
     */
    public Templates templates() {
        return templates;
    }

    /**
     * Audiências e contatos.
     *
     * @return o recurso
     */
    public Audiences audiences() {
        return audiences;
    }

    /**
     * Campanhas enviadas a audiências.
     *
     * @return o recurso
     */
    public Broadcasts broadcasts() {
        return broadcasts;
    }

    /**
     * Remetentes verificados por endereço.
     *
     * @return o recurso
     */
    public Senders senders() {
        return senders;
    }

    /**
     * Lista de supressão.
     *
     * @return o recurso
     */
    public Suppressions suppressions() {
        return suppressions;
    }

    /**
     * Endpoints de webhook e verificação de assinatura.
     *
     * @return o recurso
     */
    public Webhooks webhooks() {
        return webhooks;
    }

    /**
     * Estatísticas de entregabilidade.
     *
     * @return o recurso
     */
    public Stats stats() {
        return stats;
    }

    /**
     * Consulta os metadados da chave de API em uso, com cache de 5 minutos.
     *
     * @return os metadados da chave
     */
    public CoffeeMailResponse<ApiKeyIntrospection> introspect() {
        return introspect(false);
    }

    /**
     * Consulta os metadados da chave de API em uso.
     *
     * @param forceRefresh ignora o cache quando {@code true}
     * @return os metadados da chave
     */
    public CoffeeMailResponse<ApiKeyIntrospection> introspect(final boolean forceRefresh) {
        return runtime.introspection().introspect(forceRefresh).join();
    }

    /** Descarta o cache da introspecção da chave de API. */
    public void invalidateApiKeyCache() {
        runtime.introspection().invalidate();
    }
}
