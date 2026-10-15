package br.com.coffeemail.contract;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.coffeemail.CoffeeMail;
import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.models.audiences.CreateAudienceInput;
import br.com.coffeemail.models.audiences.CreateContactInput;
import br.com.coffeemail.models.audiences.UpdateAudienceInput;
import br.com.coffeemail.models.audiences.UpdateContactInput;
import br.com.coffeemail.models.broadcasts.BroadcastStatus;
import br.com.coffeemail.models.broadcasts.CreateBroadcastInput;
import br.com.coffeemail.models.broadcasts.ListBroadcastsQuery;
import br.com.coffeemail.models.broadcasts.TestSendBroadcastInput;
import br.com.coffeemail.models.common.OffsetPaginationQuery;
import br.com.coffeemail.models.emails.EmailAddress;
import br.com.coffeemail.models.emails.EmailAttachment;
import br.com.coffeemail.models.emails.EmailStatus;
import br.com.coffeemail.models.emails.EmailTag;
import br.com.coffeemail.models.emails.EmailType;
import br.com.coffeemail.models.emails.ListEmailsQuery;
import br.com.coffeemail.models.emails.SendEmailInput;
import br.com.coffeemail.models.senders.CreateSenderInput;
import br.com.coffeemail.models.stats.GetStatsQuery;
import br.com.coffeemail.models.stats.StatsEmailType;
import br.com.coffeemail.models.stats.StatsGranularity;
import br.com.coffeemail.models.suppressions.BulkSuppressionEntry;
import br.com.coffeemail.models.suppressions.CreateSuppressionInput;
import br.com.coffeemail.models.suppressions.CreateSuppressionReason;
import br.com.coffeemail.models.suppressions.ListSuppressionsQuery;
import br.com.coffeemail.models.suppressions.SuppressionCategory;
import br.com.coffeemail.models.suppressions.SuppressionStatus;
import br.com.coffeemail.models.templates.CreateTemplateInput;
import br.com.coffeemail.models.templates.FormatTemplateInput;
import br.com.coffeemail.models.templates.PreviewTemplateInput;
import br.com.coffeemail.models.templates.TemplateFormat;
import br.com.coffeemail.models.templates.TemplateVariable;
import br.com.coffeemail.models.templates.TemplateVariableType;
import br.com.coffeemail.models.templates.TestRenderTemplateInput;
import br.com.coffeemail.models.templates.TestSendTemplateInput;
import br.com.coffeemail.models.templates.UpdateTemplateInput;
import br.com.coffeemail.models.webhooks.CreateWebhookInput;
import br.com.coffeemail.models.webhooks.ListWebhookDeliveriesQuery;
import br.com.coffeemail.models.webhooks.ListWebhooksQuery;
import br.com.coffeemail.models.webhooks.ToggleableWebhookStatus;
import br.com.coffeemail.models.webhooks.UpdateWebhookInput;
import br.com.coffeemail.models.webhooks.WebhookDeliveryStatus;
import br.com.coffeemail.models.webhooks.WebhookEventType;
import br.com.coffeemail.models.webhooks.WebhookStatus;
import br.com.coffeemail.support.FakeTransport;
import br.com.coffeemail.support.Fixtures;
import br.com.coffeemail.transport.HttpRequest;
import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.SimpleRequest;
import com.atlassian.oai.validator.model.SimpleResponse;
import com.atlassian.oai.validator.report.ValidationReport;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

final class ContractConformanceTest {

    private static final Path SPEC = Path.of("spec/product.json");
    private static final OpenApiInteractionValidator VALIDATOR = OpenApiInteractionValidator.createForSpecificationUrl(
                    SPEC.toUri().toString())
            .build();
    private static final Set<String> OUT_OF_SCOPE = Set.of("GET /v1/product/auth/csrf");
    private static final Instant AT = Instant.parse("2026-10-14T12:00:00Z");
    private static final String PLACEHOLDER = "\\{[^}]+}";
    private static final String NO_RESPONSE_SCHEMA = "operação sem schema de resposta no product.json";
    private static final String DELETE_RETURNS_204 = "a API responde 204, mas o product.json declara outro status";
    private static final String NO_CONTENT_DECLARES_BODY =
            "a API responde 204 sem corpo, mas o product.json declara um corpo nulo";
    private static final Map<String, String> RESPONSE_SPEC_GAPS = Map.ofEntries(
            Map.entry("client.introspect", NO_RESPONSE_SCHEMA),
            Map.entry("audiences.list", NO_RESPONSE_SCHEMA),
            Map.entry("audiences.get", NO_RESPONSE_SCHEMA),
            Map.entry("audiences.update", NO_RESPONSE_SCHEMA),
            Map.entry("contacts.update", NO_RESPONSE_SCHEMA),
            Map.entry("broadcasts.send", NO_RESPONSE_SCHEMA),
            Map.entry("webhooks.toggle", NO_RESPONSE_SCHEMA),
            Map.entry("webhooks.rotateSecret", NO_RESPONSE_SCHEMA),
            Map.entry("domains.delete", NO_RESPONSE_SCHEMA),
            Map.entry("emails.cancel", DELETE_RETURNS_204),
            Map.entry("templates.delete", DELETE_RETURNS_204),
            Map.entry("audiences.delete", DELETE_RETURNS_204),
            Map.entry("contacts.delete", DELETE_RETURNS_204),
            Map.entry("senders.delete", DELETE_RETURNS_204),
            Map.entry("senders.resendVerification", NO_CONTENT_DECLARES_BODY),
            Map.entry("suppressions.delete", DELETE_RETURNS_204),
            Map.entry("suppressions.reactivate", DELETE_RETURNS_204),
            Map.entry("webhooks.delete", DELETE_RETURNS_204));
    private static final Map<String, String> REQUEST_SPEC_GAPS = Map.of(
            "domains.delete",
            "validation.request.body.missing: o product.json exige a credencial de reautenticação, usada só por"
                    + " sessões do dashboard; a API dispensa para chaves de API");

    record ContractCase(String fixture, String operation, Function<CoffeeMail, CoffeeMailResponse<?>> call) {
        @Override
        public String toString() {
            return fixture + " → " + operation;
        }
    }

    private static ContractCase c(
            final String fixture, final String operation, final Function<CoffeeMail, CoffeeMailResponse<?>> call) {
        return new ContractCase(fixture, operation, call);
    }

    private static String id(final String name) {
        return Fixtures.id(name);
    }

    static Stream<ContractCase> cases() {
        return Stream.of(emailCases(), domainCases(), templateCases(), audienceCases(), broadcastCases(), otherCases())
                .flatMap(Function.identity());
    }

    private static Stream<ContractCase> emailCases() {
        final SendEmailInput full = SendEmailInput.builder("Loja <contato@loja.com.br>", "cliente@gmail.com")
                .cc("copia@gmail.com")
                .bcc(List.of(EmailAddress.of("oculta@gmail.com", "Oculta")))
                .replyTo("suporte@loja.com.br")
                .subject("Pedido confirmado")
                .html("<h1>Obrigado</h1>")
                .text("Obrigado")
                .headers(Map.of("X-Pedido", "123"))
                .attachments(EmailAttachment.fromBytes("nota.txt", "nota".getBytes(StandardCharsets.UTF_8))
                        .withContentType("text/plain"))
                .tags(new EmailTag("plano", "pro"))
                .variables(Map.of("nome", "Ana"))
                .emailType(EmailType.TRANSACTIONAL)
                .suppressionCategory("pedidos")
                .scheduledAt(AT)
                .metadata(Map.of("pedido", 123))
                .idempotencyKey("pedido-123")
                .sandbox(true)
                .build();
        final SendEmailInput withTemplate = SendEmailInput.builder("contato@loja.com.br", "cliente@gmail.com")
                .templateId(id("template"))
                .variables(Map.of("nome", "Ana"))
                .build();
        return Stream.of(
                c(
                        "emails.send",
                        "POST /v1/product/emails",
                        client -> client.emails().send(full)),
                c(
                        "emails.sendBatch",
                        "POST /v1/product/emails/batch",
                        client -> client.emails().sendBatch(List.of(full, withTemplate))),
                c(
                        "emails.list",
                        "GET /v1/product/emails",
                        client -> client.emails()
                                .list(ListEmailsQuery.builder()
                                        .status(EmailStatus.DELIVERED)
                                        .recipient("cliente@gmail.com")
                                        .fromEmail("contato@loja.com.br")
                                        .subjectContains("Pedido")
                                        .apiKeyId(id("key"))
                                        .tag("plano")
                                        .from(AT)
                                        .to(AT)
                                        .after("c_1")
                                        .limit(50)
                                        .build())),
                c(
                        "emails.getTags",
                        "GET /v1/product/emails/tags",
                        client -> client.emails().getTags()),
                c(
                        "emails.get",
                        "GET /v1/product/emails/{id}",
                        client -> client.emails().get(id("email"))),
                c(
                        "emails.getEvents",
                        "GET /v1/product/emails/{id}/events",
                        client -> client.emails().getEvents(id("email"))),
                c(
                        "emails.cancel",
                        "POST /v1/product/emails/{id}/cancel",
                        client -> client.emails().cancel(id("email"))),
                c(
                        "emails.resend",
                        "POST /v1/product/emails/{id}/resend",
                        client -> client.emails().resend(id("email"))));
    }

    private static Stream<ContractCase> domainCases() {
        return Stream.of(
                c(
                        "domains.create",
                        "POST /v1/product/domains",
                        client -> client.domains().create("loja.com.br")),
                c(
                        "domains.list",
                        "GET /v1/product/domains",
                        client -> client.domains().list()),
                c(
                        "domains.get",
                        "GET /v1/product/domains/{id}",
                        client -> client.domains().get(id("domain"))),
                c(
                        "domains.delete",
                        "DELETE /v1/product/domains/{id}",
                        client -> client.domains().delete(id("domain"))),
                c(
                        "domains.verify",
                        "POST /v1/product/domains/{id}/verify",
                        client -> client.domains().verify(id("domain"))),
                c(
                        "domains.getHealth",
                        "POST /v1/product/domains/{id}/health",
                        client -> client.domains().getHealth(id("domain"))),
                c(
                        "domains.getWarmupStatus",
                        "GET /v1/product/domains/{id}/warmup",
                        client -> client.domains().getWarmupStatus(id("domain"))));
    }

    private static Stream<ContractCase> templateCases() {
        final TemplateVariable variable = TemplateVariable.of("nome")
                .withType(TemplateVariableType.STRING)
                .withFallbackValue("cliente")
                .withDescription("Primeiro nome");
        return Stream.of(
                c(
                        "templates.create",
                        "POST /v1/product/templates",
                        client -> client.templates()
                                .create(CreateTemplateInput.builder("Boas-vindas", "<h1>Olá {{nome}}</h1>")
                                        .alias("boas-vindas")
                                        .subject("Bem-vindo")
                                        .preheader("Que bom ter você")
                                        .contentJson(Map.of("blocks", List.of()))
                                        .textPayload("Olá")
                                        .variables(List.of(variable))
                                        .format(TemplateFormat.HTML)
                                        .sourceLocale("pt-BR")
                                        .build())),
                c(
                        "templates.list",
                        "GET /v1/product/templates",
                        client -> client.templates().list()),
                c(
                        "templates.get",
                        "GET /v1/product/templates/{id}",
                        client -> client.templates().get(id("template"))),
                c(
                        "templates.update",
                        "PATCH /v1/product/templates/{id}",
                        client -> client.templates()
                                .update(
                                        id("template"),
                                        UpdateTemplateInput.builder()
                                                .name("Boas-vindas v2")
                                                .clearAlias()
                                                .subject("Olá!")
                                                .html("<h1>Oi</h1>")
                                                .variables(List.of(variable))
                                                .format(TemplateFormat.HTML)
                                                .isActive(true)
                                                .sourceLocale("en")
                                                .build())),
                c(
                        "templates.delete",
                        "DELETE /v1/product/templates/{id}",
                        client -> client.templates().delete(id("template"))),
                c(
                        "templates.preview",
                        "POST /v1/product/templates/preview",
                        client -> client.templates()
                                .preview(PreviewTemplateInput.builder("<h1>{{nome}}</h1>")
                                        .format(TemplateFormat.HTML)
                                        .variables(Map.of("nome", "Ana"))
                                        .build())),
                c(
                        "templates.format",
                        "POST /v1/product/templates/format",
                        client -> client.templates()
                                .format(FormatTemplateInput.builder("<h1>Olá</h1>")
                                        .format(TemplateFormat.REACT)
                                        .build())),
                c(
                        "templates.testRender",
                        "POST /v1/product/templates/test-render",
                        client -> client.templates()
                                .testRender(TestRenderTemplateInput.builder("<h1>{{nome}}</h1>")
                                        .format(TemplateFormat.HTML)
                                        .variables(Map.of("nome", "Ana"))
                                        .build())),
                c(
                        "templates.testSend",
                        "POST /v1/product/templates/{id}/test-send",
                        client -> client.templates()
                                .testSend(
                                        id("template"),
                                        TestSendTemplateInput.builder("teste@loja.com.br")
                                                .from("contato@loja.com.br")
                                                .subject("Teste")
                                                .variables(Map.of("nome", "Ana"))
                                                .build())));
    }

    private static Stream<ContractCase> audienceCases() {
        final CreateContactInput contact = CreateContactInput.builder("cliente@gmail.com")
                .firstName("Ana")
                .lastName("Souza")
                .metadata(Map.of("plano", "pro"))
                .build();
        final OffsetPaginationQuery page =
                OffsetPaginationQuery.builder().limit(10).offset(20).build();
        return Stream.of(
                c(
                        "audiences.create",
                        "POST /v1/product/audiences",
                        client -> client.audiences()
                                .create(CreateAudienceInput.builder("Clientes")
                                        .description("Base ativa")
                                        .build())),
                c(
                        "audiences.list",
                        "GET /v1/product/audiences",
                        client -> client.audiences().list(page)),
                c(
                        "audiences.get",
                        "GET /v1/product/audiences/{id}",
                        client -> client.audiences().get(id("audience"))),
                c(
                        "audiences.update",
                        "PUT /v1/product/audiences/{id}",
                        client -> client.audiences()
                                .update(
                                        id("audience"),
                                        UpdateAudienceInput.builder()
                                                .name("VIP")
                                                .clearDescription()
                                                .active(false)
                                                .build())),
                c(
                        "audiences.delete",
                        "DELETE /v1/product/audiences/{id}",
                        client -> client.audiences().delete(id("audience"))),
                c(
                        "contacts.create",
                        "POST /v1/product/audiences/{id}/contacts",
                        client -> client.audiences().contacts().create(id("audience"), contact)),
                c(
                        "contacts.list",
                        "GET /v1/product/audiences/{id}/contacts",
                        client -> client.audiences().contacts().list(id("audience"), page)),
                c(
                        "contacts.bulkAdd",
                        "POST /v1/product/audiences/{id}/contacts/bulk",
                        client -> client.audiences()
                                .contacts()
                                .bulkAdd(
                                        id("audience"),
                                        List.of(
                                                contact,
                                                CreateContactInput.builder("b@gmail.com")
                                                        .build()))),
                c(
                        "contacts.update",
                        "PUT /v1/product/audiences/{id}/contacts/{contactId}",
                        client -> client.audiences()
                                .contacts()
                                .update(
                                        id("audience"),
                                        id("contact"),
                                        UpdateContactInput.builder()
                                                .firstName("Ana")
                                                .clearLastName()
                                                .metadata(Map.of("plano", "free"))
                                                .build())),
                c(
                        "contacts.delete",
                        "DELETE /v1/product/audiences/{id}/contacts/{contactId}",
                        client -> client.audiences().contacts().delete(id("audience"), id("contact"))));
    }

    private static Stream<ContractCase> broadcastCases() {
        return Stream.of(
                c(
                        "broadcasts.create",
                        "POST /v1/product/broadcasts",
                        client -> client.broadcasts()
                                .create(CreateBroadcastInput.fromHtml(
                                                id("audience"), "news@loja.com.br", "Novidades", "<h1>Novidades</h1>")
                                        .text("Novidades")
                                        .replyTo("suporte@loja.com.br")
                                        .variables(Map.of("cupom", "OUTUBRO"))
                                        .build())),
                c(
                        "broadcasts.list",
                        "GET /v1/product/broadcasts",
                        client -> client.broadcasts()
                                .list(ListBroadcastsQuery.builder()
                                        .status(BroadcastStatus.SENT)
                                        .from(AT)
                                        .to(AT)
                                        .after("c_1")
                                        .limit(20)
                                        .build())),
                c(
                        "broadcasts.get",
                        "GET /v1/product/broadcasts/{id}",
                        client -> client.broadcasts().get(id("broadcast"))),
                c(
                        "broadcasts.send",
                        "POST /v1/product/broadcasts/{id}/send",
                        client -> client.broadcasts().send(id("broadcast"))),
                c(
                        "broadcasts.testSend",
                        "POST /v1/product/broadcasts/{id}/test-send",
                        client -> client.broadcasts()
                                .testSend(
                                        id("broadcast"),
                                        TestSendBroadcastInput.builder("teste@loja.com.br")
                                                .variables(Map.of("cupom", "OUTUBRO"))
                                                .build())),
                c(
                        "broadcasts.cancel",
                        "POST /v1/product/broadcasts/{id}/cancel",
                        client -> client.broadcasts().cancel(id("broadcast"))));
    }

    private static Stream<ContractCase> otherCases() {
        return Stream.of(
                c(
                        "senders.create",
                        "POST /v1/product/senders",
                        client -> client.senders()
                                .create(CreateSenderInput.builder("joao@loja.com.br")
                                        .displayName("João")
                                        .build())),
                c(
                        "senders.list",
                        "GET /v1/product/senders",
                        client -> client.senders().list()),
                c(
                        "senders.verify",
                        "POST /v1/product/senders/verify",
                        client -> client.senders().verify(id("token"))),
                c(
                        "senders.resendVerification",
                        "POST /v1/product/senders/{id}/resend-verification",
                        client -> client.senders().resendVerification(id("sender"))),
                c(
                        "senders.delete",
                        "DELETE /v1/product/senders/{id}",
                        client -> client.senders().delete(id("sender"))),
                c(
                        "suppressions.create",
                        "POST /v1/product/suppressions",
                        client -> client.suppressions()
                                .create(CreateSuppressionInput.builder("bloqueado@gmail.com")
                                        .reason(CreateSuppressionReason.COMPLAINT)
                                        .expiresAt(AT)
                                        .build())),
                c(
                        "suppressions.list",
                        "GET /v1/product/suppressions",
                        client -> client.suppressions()
                                .list(ListSuppressionsQuery.builder()
                                        .status(SuppressionStatus.ACTIVE)
                                        .category(SuppressionCategory.INVALID_EMAIL)
                                        .after("c_1")
                                        .limit(200)
                                        .build())),
                c(
                        "suppressions.bulkCreate",
                        "POST /v1/product/suppressions/bulk",
                        client -> client.suppressions()
                                .bulkCreate(List.of(
                                        BulkSuppressionEntry.builder("a@gmail.com")
                                                .reason(CreateSuppressionReason.MANUAL)
                                                .build(),
                                        BulkSuppressionEntry.builder("b@gmail.com")
                                                .build()))),
                c(
                        "suppressions.get",
                        "GET /v1/product/suppressions/{id}",
                        client -> client.suppressions().get(id("suppression"))),
                c(
                        "suppressions.delete",
                        "DELETE /v1/product/suppressions/{id}",
                        client -> client.suppressions().delete(id("suppression"))),
                c(
                        "suppressions.reactivate",
                        "POST /v1/product/suppressions/{id}/reactivate",
                        client -> client.suppressions().reactivate(id("suppression"))),
                c(
                        "webhooks.create",
                        "POST /v1/product/webhooks",
                        client -> client.webhooks()
                                .create(CreateWebhookInput.builder(
                                                "https://loja.com.br/webhooks",
                                                List.of(
                                                        WebhookEventType.EMAIL_DELIVERED,
                                                        WebhookEventType.EMAIL_BOUNCED))
                                        .description("Produção")
                                        .build())),
                c(
                        "webhooks.list",
                        "GET /v1/product/webhooks",
                        client -> client.webhooks()
                                .list(ListWebhooksQuery.builder()
                                        .status(WebhookStatus.ACTIVE)
                                        .build())),
                c(
                        "webhooks.get",
                        "GET /v1/product/webhooks/{id}",
                        client -> client.webhooks().get(id("webhook"))),
                c(
                        "webhooks.update",
                        "PUT /v1/product/webhooks/{id}",
                        client -> client.webhooks()
                                .update(
                                        id("webhook"),
                                        UpdateWebhookInput.builder()
                                                .url("https://loja.com.br/v2/webhooks")
                                                .events(List.of(WebhookEventType.EMAIL_OPENED))
                                                .clearDescription()
                                                .build())),
                c(
                        "webhooks.toggle",
                        "PATCH /v1/product/webhooks/{id}",
                        client -> client.webhooks().toggle(id("webhook"), ToggleableWebhookStatus.PAUSED)),
                c(
                        "webhooks.delete",
                        "DELETE /v1/product/webhooks/{id}",
                        client -> client.webhooks().delete(id("webhook"))),
                c(
                        "webhooks.rotateSecret",
                        "POST /v1/product/webhooks/{id}/rotate-secret",
                        client -> client.webhooks().rotateSecret(id("webhook"))),
                c(
                        "webhooks.listDeliveries",
                        "GET /v1/product/webhooks/{id}/deliveries",
                        client -> client.webhooks()
                                .listDeliveries(
                                        id("webhook"),
                                        ListWebhookDeliveriesQuery.builder()
                                                .eventType("email.delivered")
                                                .status(WebhookDeliveryStatus.FAILED)
                                                .from(AT)
                                                .to(AT)
                                                .limit(10)
                                                .build())),
                c(
                        "webhooks.test",
                        "POST /v1/product/webhooks/{id}/test",
                        client -> client.webhooks().test(id("webhook"))),
                c(
                        "stats.get",
                        "GET /v1/product/stats",
                        client -> client.stats()
                                .get(GetStatsQuery.builder()
                                        .domainId(id("domain"))
                                        .tag("plano")
                                        .startDate("2026-10-01")
                                        .endDate("2026-10-14")
                                        .granularity(StatsGranularity.WEEK)
                                        .emailType(StatsEmailType.TRANSACTIONAL)
                                        .build())),
                c("client.introspect", "GET /v1/product/auth/me/api-key", CoffeeMail::introspect));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("cases")
    void CA23_requestIsValidAgainstTheContractAndFixtureDecodes(final ContractCase contractCase) {
        final Fixtures.Fixture fixture = Fixtures.response(contractCase.fixture());
        final FakeTransport transport = FakeTransport.replying(fixture.status(), fixture.body());
        final CoffeeMail client = CoffeeMail.builder()
                .apiKey("cm_live_contract")
                .transport(transport)
                .build();

        final CoffeeMailResponse<?> response = contractCase.call().apply(client);

        final HttpRequest request = transport.lastRequest();
        final String[] operation = contractCase.operation().split(" ", 2);
        assertThat(request.method().name()).isEqualTo(operation[0]);
        assertThat(request.uri().getPath()).matches(operation[1].replaceAll(PLACEHOLDER, "[^/]+"));
        assertThat(messages(VALIDATOR.validateRequest(toValidatorRequest(request))))
                .as("request de %s", contractCase)
                .allSatisfy(message -> assertThat(REQUEST_SPEC_GAPS.getOrDefault(contractCase.fixture(), ""))
                        .startsWith(message.substring(0, message.indexOf(':'))));
        assertThat(
                        RESPONSE_SPEC_GAPS.containsKey(contractCase.fixture())
                                ? List.<String>of()
                                : messages(validateFixture(operation, fixture)))
                .as("fixture de %s", contractCase)
                .isEmpty();
        assertThat(response.error()).as("decodificação de %s", contractCase).isEmpty();
    }

    @Test
    void CA23_knownSpecGapsStillFailValidation() {
        assertThat(RESPONSE_SPEC_GAPS.keySet()).allSatisfy(name -> {
            final ContractCase gap = cases().filter(item -> item.fixture().equals(name))
                    .findFirst()
                    .orElseThrow();
            assertThat(messages(validateFixture(gap.operation().split(" ", 2), Fixtures.response(name))))
                    .as("%s ainda é lacuna do spec; se o spec foi corrigido, remova-o da lista", name)
                    .isNotEmpty();
        });
    }

    @Test
    void CA23_everyOperationOfTheContractIsCovered() {
        final Set<String> covered = cases().map(ContractCase::operation).collect(Collectors.toUnmodifiableSet());

        assertThat(Fixtures.specOperations())
                .filteredOn(operation -> !OUT_OF_SCOPE.contains(operation))
                .containsExactlyInAnyOrderElementsOf(covered);
    }

    private static ValidationReport validateFixture(final String[] operation, final Fixtures.Fixture fixture) {
        final SimpleResponse.Builder builder = SimpleResponse.Builder.status(fixture.status());
        final SimpleResponse response = fixture.body().isEmpty()
                ? builder.build()
                : builder.withContentType("application/json")
                        .withBody(fixture.body())
                        .build();
        return VALIDATOR.validateResponse(operation[1], Request.Method.valueOf(operation[0]), response);
    }

    private static SimpleRequest toValidatorRequest(final HttpRequest request) {
        final SimpleRequest.Builder builder =
                new SimpleRequest.Builder(request.method().name(), request.uri().getPath());
        request.headers().forEach(builder::withHeader);
        queryParams(request).forEach(builder::withQueryParam);
        return request.body().map(builder::withBody).orElse(builder).build();
    }

    private static Map<String, List<String>> queryParams(final HttpRequest request) {
        return Optional.ofNullable(request.uri().getRawQuery()).stream()
                .flatMap(query -> Arrays.stream(query.split("&")))
                .map(pair -> pair.split("=", 2))
                .collect(Collectors.groupingBy(
                        pair -> decode(pair[0]), Collectors.mapping(pair -> decode(pair[1]), Collectors.toList())));
    }

    private static String decode(final String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static List<String> messages(final ValidationReport report) {
        return report.getMessages().stream()
                .filter(message -> message.getLevel() == ValidationReport.Level.ERROR)
                .map(message -> message.getKey() + ": " + message.getMessage())
                .toList();
    }
}
