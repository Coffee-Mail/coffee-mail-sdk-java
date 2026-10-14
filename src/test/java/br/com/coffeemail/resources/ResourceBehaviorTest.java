package br.com.coffeemail.resources;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.coffeemail.CoffeeMail;
import br.com.coffeemail.CoffeeMailLocale;
import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.errors.NotFoundException;
import br.com.coffeemail.errors.PermissionException;
import br.com.coffeemail.internal.result.TryCatch;
import br.com.coffeemail.models.audiences.AudienceDetail;
import br.com.coffeemail.models.broadcasts.CreateBroadcastInput;
import br.com.coffeemail.models.domains.CreateDomainResponse;
import br.com.coffeemail.models.domains.DomainWarmupStatus;
import br.com.coffeemail.models.emails.AttachmentDisposition;
import br.com.coffeemail.models.emails.EmailAddress;
import br.com.coffeemail.models.emails.EmailAttachment;
import br.com.coffeemail.models.emails.EmailDetail;
import br.com.coffeemail.models.emails.EmailStatus;
import br.com.coffeemail.models.emails.EmailType;
import br.com.coffeemail.models.emails.SendEmailInput;
import br.com.coffeemail.models.templates.ListTemplatesResponse;
import br.com.coffeemail.models.templates.PreviewTemplateResponse;
import br.com.coffeemail.models.templates.UpdateTemplateInput;
import br.com.coffeemail.support.FakeTransport;
import br.com.coffeemail.support.Fixtures;
import br.com.coffeemail.transport.HttpRequest;
import br.com.coffeemail.transport.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

final class ResourceBehaviorTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String FORBIDDEN_BODY =
            "{\"error\":{\"code\":\"INSUFFICIENT_SCOPE\",\"message\":\"proibido\",\"meta\":{\"scope\":\"sending_access\"}}}";

    private static CoffeeMail client(final FakeTransport transport) {
        return CoffeeMail.builder().apiKey("cm_live_test").transport(transport).build();
    }

    private static JsonNode body(final HttpRequest request) {
        return request.body().map(ResourceBehaviorTest::parse).orElseThrow();
    }

    private static JsonNode parse(final String json) {
        return TryCatch.of(() -> MAPPER.readTree(json)).<JsonNode>fold(node -> node, error -> MAPPER.nullNode());
    }

    @Test
    void CA11_addressesAreNormalizedAndRecipientIsWrappedInAList() {
        final FakeTransport transport = FakeTransport.replying(202, Fixtures.body("emails.send"));

        client(transport)
                .emails()
                .send(SendEmailInput.builder("  \"Loja Ana\" <contato@loja.com.br> ", "cliente@gmail.com")
                        .html("<p>oi</p>")
                        .build());

        final JsonNode body = body(transport.lastRequest());
        assertThat(body.path("from").path("email").asText()).isEqualTo("contato@loja.com.br");
        assertThat(body.path("from").path("name").asText()).isEqualTo("Loja Ana");
        assertThat(body.path("to").isArray()).isTrue();
        assertThat(body.path("to").get(0).path("email").asText()).isEqualTo("cliente@gmail.com");
        assertThat(body.path("to").get(0).has("name")).isFalse();
    }

    @Test
    void CA11_emailAddressParsingCoversEdgeCases() {
        assertThat(EmailAddress.parse("a@b.com")).isEqualTo(new EmailAddress("a@b.com", Optional.empty()));
        assertThat(EmailAddress.parse("<a@b.com>")).isEqualTo(new EmailAddress("a@b.com", Optional.empty()));
        assertThat(EmailAddress.parse("'Ana' <a@b.com>")).isEqualTo(EmailAddress.of("a@b.com", "Ana"));
        assertThat(EmailAddress.parse("sem arroba e com espaço"))
                .isEqualTo(new EmailAddress("sem arroba e com espaço", Optional.empty()));
        assertThat(EmailAddress.of(" a@b.com ", "  ")).isEqualTo(new EmailAddress("a@b.com", Optional.empty()));
    }

    @Test
    void CA12_idempotencyAndSandboxGoOnlyToHeaders() {
        final FakeTransport transport = FakeTransport.replying(202, Fixtures.body("emails.send"));

        client(transport)
                .emails()
                .send(SendEmailInput.builder("a@loja.com.br", "b@gmail.com")
                        .subject("x")
                        .idempotencyKey("pedido-1")
                        .sandbox(true)
                        .build());

        final HttpRequest request = transport.lastRequest();
        assertThat(request.headers())
                .containsEntry("x-idempotency-key", "pedido-1")
                .containsEntry("x-coffeemail-sandbox", "true");
        assertThat(body(request).has("idempotencyKey")).isFalse();
        assertThat(body(request).has("sandbox")).isFalse();
    }

    @Test
    void CA12_headersAreAbsentWhenNotRequested() {
        final FakeTransport transport = FakeTransport.replying(202, Fixtures.body("emails.send"));

        client(transport)
                .emails()
                .send(SendEmailInput.builder("a@loja.com.br", "b@gmail.com").build());

        assertThat(transport.lastRequest().headers()).doesNotContainKeys("x-idempotency-key", "x-coffeemail-sandbox");
    }

    @Test
    void CA13_emailTypeSuppressionCategoryAndMetadataAreSent() {
        final FakeTransport transport = FakeTransport.replying(202, Fixtures.body("emails.send"));

        client(transport)
                .emails()
                .send(SendEmailInput.builder("a@loja.com.br", "b@gmail.com")
                        .emailType(EmailType.BROADCAST)
                        .suppressionCategory("newsletter")
                        .metadata(Map.of("pedido", 42))
                        .build());

        final JsonNode body = body(transport.lastRequest());
        assertThat(body.path("emailType").asText()).isEqualTo("broadcast");
        assertThat(body.path("suppressionCategory").asText()).isEqualTo("newsletter");
        assertThat(body.path("metadata").path("pedido").asInt()).isEqualTo(42);
    }

    @Test
    void CA14_attachmentsAreBase64WithDefaults() {
        final FakeTransport transport = FakeTransport.replying(202, Fixtures.body("emails.send"));
        final EmailAttachment inline = EmailAttachment.fromBase64("logo.png", "iVBORw0KGgo=")
                .withContentType("image/png")
                .withCid("logo");

        client(transport)
                .emails()
                .send(SendEmailInput.builder("a@loja.com.br", "b@gmail.com")
                        .attachments(
                                EmailAttachment.fromBytes("nota.txt", "olá".getBytes(StandardCharsets.UTF_8)), inline)
                        .build());

        final JsonNode attachments = body(transport.lastRequest()).path("attachments");
        assertThat(attachments.get(0).path("content").asText()).isEqualTo("b2zDoQ==");
        assertThat(attachments.get(0).path("contentType").asText()).isEqualTo("application/octet-stream");
        assertThat(attachments.get(0).path("disposition").asText()).isEqualTo("attachment");
        assertThat(attachments.get(0).has("cid")).isFalse();
        assertThat(attachments.get(1).path("disposition").asText()).isEqualTo("inline");
        assertThat(attachments.get(1).path("cid").asText()).isEqualTo("logo");
        assertThat(inline.withDisposition(AttachmentDisposition.ATTACHMENT).disposition())
                .isEqualTo(AttachmentDisposition.ATTACHMENT);
    }

    @Test
    void CA15_batchMergesIdempotencyAndSandboxHeaders() {
        final FakeTransport transport = FakeTransport.replying(202, Fixtures.body("emails.sendBatch"));

        final CoffeeMailResponse<?> response = client(transport)
                .emails()
                .sendBatch(List.of(
                        SendEmailInput.builder("a@loja.com.br", "b@gmail.com")
                                .idempotencyKey("lote-1")
                                .build(),
                        SendEmailInput.builder("a@loja.com.br", "c@gmail.com")
                                .sandbox(true)
                                .build()));

        assertThat(transport.lastRequest().headers())
                .containsEntry("x-idempotency-key", "lote-1")
                .containsEntry("x-coffeemail-sandbox", "true");
        assertThat(body(transport.lastRequest()).size()).isEqualTo(2);
        assertThat(response.isSuccess()).isTrue();
    }

    @Test
    void CA16_emptyCollectionsAndAbsentFieldsAreNotSerialized() {
        final FakeTransport transport = FakeTransport.replying(202, Fixtures.body("emails.send"));

        client(transport)
                .emails()
                .send(SendEmailInput.builder("a@loja.com.br", "b@gmail.com")
                        .cc(List.of())
                        .tags(List.of())
                        .html("")
                        .variables(Map.of())
                        .build());

        assertThat(body(transport.lastRequest()).properties())
                .extracting(Map.Entry::getKey)
                .containsExactlyInAnyOrder("from", "to");
    }

    @Test
    void CA16_patchInputsSendOnlyDefinedFieldsAndExplicitNulls() {
        final FakeTransport transport = FakeTransport.replying(200, Fixtures.body("templates.update"));

        client(transport)
                .templates()
                .update(
                        "tpl_1",
                        UpdateTemplateInput.builder().name("Novo").clearAlias().build());

        assertThat(transport.lastRequest().body()).contains("{\"name\":\"Novo\",\"alias\":null}");
    }

    @Test
    @SuppressWarnings("deprecation")
    void CA17_templatesListAcceptsDataOrTemplates() {
        final ListTemplatesResponse fromData = client(FakeTransport.replying(200, Fixtures.body("templates.list")))
                .templates()
                .list()
                .unwrap();
        final ListTemplatesResponse fromTemplates = client(FakeTransport.replying(
                        200,
                        "{\"templates\":"
                                + parse(Fixtures.body("templates.list")).path("data") + ",\"nextCursor\":\"c\"}"))
                .templates()
                .list()
                .unwrap();

        assertThat(fromData.templates()).hasSize(1);
        assertThat(fromData.templates()).isEqualTo(fromData.data());
        assertThat(fromTemplates.templates()).hasSize(1).isEqualTo(fromTemplates.data());
        assertThat(fromTemplates.nextCursor()).contains("c");
    }

    @Test
    void CA18_previewByIdStopsWhenTheTemplateIsNotFound() {
        final FakeTransport transport =
                FakeTransport.replying(404, "{\"error\":{\"code\":\"TEMPLATE_NOT_FOUND\",\"message\":\"x\"}}");

        final CoffeeMailResponse<PreviewTemplateResponse> response =
                client(transport).templates().previewById("tpl_1", Map.of("nome", "Ana"));

        assertThat(response.error()).containsInstanceOf(NotFoundException.class);
        assertThat(transport.requests()).hasSize(1);
    }

    @Test
    void CA18_previewByIdRendersTheStoredTemplate() {
        final AtomicInteger calls = new AtomicInteger();
        final FakeTransport transport =
                FakeTransport.responding(request -> CompletableFuture.completedFuture(new HttpResponse(
                        200,
                        Map.of(),
                        calls.getAndIncrement() == 0
                                ? Fixtures.body("templates.get")
                                : Fixtures.body("templates.preview"))));

        final PreviewTemplateResponse response = client(transport)
                .templates()
                .previewById("tpl_1", Map.of("nome", "Ana"))
                .unwrap();

        final JsonNode previewBody = body(transport.lastRequest());
        assertThat(response.html()).isEqualTo("<h1>Olá Ana</h1>");
        assertThat(transport.lastRequest().uri().getPath()).isEqualTo("/v1/product/templates/preview");
        assertThat(previewBody.path("html").asText()).isEqualTo("<h1>Olá {{nome}}</h1>");
        assertThat(previewBody.path("format").asText()).isEqualTo("html");
        assertThat(previewBody.path("variables").path("nome").asText()).isEqualTo("Ana");
    }

    @Test
    @SuppressWarnings("deprecation")
    void CA19_audienceCountsAreEquivalent() {
        final AudienceDetail fromLegacy = client(FakeTransport.replying(200, Fixtures.body("audiences.get")))
                .audiences()
                .get("a")
                .unwrap();
        final AudienceDetail fromNew = client(FakeTransport.replying(
                        200,
                        Fixtures.body("audiences.get")
                                .replace("\"contactsCount\": 42", "\"totalContacts\": 7")
                                .replace("\"contactsCount\":42", "\"totalContacts\":7")))
                .audiences()
                .get("a")
                .unwrap();

        assertThat(fromLegacy.totalContacts()).isEqualTo(42);
        assertThat(fromNew.totalContacts()).isEqualTo(7);
        assertThat(fromNew.contactsCount()).isEqualTo(7);
    }

    @Test
    void CA20_forbiddenOnBroadcastsBecomesPermissionError() {
        final FakeTransport transport = FakeTransport.replying(403, FORBIDDEN_BODY, Map.of("x-request-id", "r9"));

        final PermissionException error = (PermissionException)
                client(transport).broadcasts().send("brd_1").error().orElseThrow();

        assertThat(error.requiredPermission()).isEqualTo("full_access");
        assertThat(error.code()).isEqualTo("PERMISSION_DENIED");
        assertThat(error.apiCode()).contains("INSUFFICIENT_SCOPE");
        assertThat(error.requestId()).contains("r9");
        assertThat(error.details())
                .containsEntry("operation", "broadcasts.send")
                .containsEntry("scope", "sending_access");
        assertThat(error).hasMessageContaining("full_access (called by broadcasts.send)");
    }

    @Test
    void CA20_permissionMessageFollowsLocaleAndOtherErrorsAreKept() {
        final CoffeeMail english = CoffeeMail.builder()
                .apiKey("cm_live_test")
                .locale(CoffeeMailLocale.EN)
                .transport(FakeTransport.replying(403, FORBIDDEN_BODY))
                .build();
        final CoffeeMail notFound = client(FakeTransport.replying(404, "{\"error\":{\"message\":\"x\"}}"));

        assertThat(english.broadcasts().list().error().orElseThrow())
                .hasMessageStartingWith("This API key lacks the required permission");
        assertThat(notFound.broadcasts().get("brd_1").error()).containsInstanceOf(NotFoundException.class);
        assertThat(client(FakeTransport.replying(403, FORBIDDEN_BODY))
                        .emails()
                        .get("e")
                        .error()
                        .orElseThrow())
                .isNotInstanceOf(PermissionException.class);
    }

    @Test
    void CA21_broadcastInputCarriesExactlyTemplateOrHtml() {
        final CreateBroadcastInput fromTemplate = CreateBroadcastInput.fromTemplate("aud", "news@loja.com.br", "tpl")
                .subject("Assunto")
                .build();
        final CreateBroadcastInput fromHtml = CreateBroadcastInput.fromHtml(
                        "aud", "news@loja.com.br", "Assunto", "<h1>x</h1>")
                .build();

        assertThat(fromTemplate.fields()).containsOnlyKeys("audienceId", "fromEmail", "templateId", "subject");
        assertThat(fromHtml.fields()).containsOnlyKeys("audienceId", "fromEmail", "subject", "html");
        assertThat(fromHtml)
                .isEqualTo(CreateBroadcastInput.fromHtml("aud", "news@loja.com.br", "Assunto", "<h1>x</h1>")
                        .build());
        assertThat(fromHtml.hashCode()).isEqualTo(fromHtml.fields().hashCode());
        assertThat(fromHtml.toString()).startsWith("CreateBroadcastInput{");
    }

    @Test
    void CA22_identifiersAreEncodedInThePath() {
        final FakeTransport transport = FakeTransport.replying(200, Fixtures.body("emails.get"));

        client(transport).emails().get("../admin?x=1");

        assertThat(transport.lastRequest().uri().getRawPath()).isEqualTo("/v1/product/emails/..%2Fadmin%3Fx%3D1");
    }

    @Test
    void CA24_unknownEnumValuesDeserializeAsUnknown() {
        final EmailDetail detail = client(FakeTransport.replying(
                        200, Fixtures.body("emails.get").replace("\"delivered\"", "\"quarantined\"")))
                .emails()
                .get("e")
                .unwrap();

        assertThat(detail.status()).isEqualTo(EmailStatus.UNKNOWN);
    }

    @Test
    void nullAndMissingOptionalFieldsBecomeEmpty() {
        final CreateDomainResponse created = client(FakeTransport.replying(201, Fixtures.body("domains.create")))
                .domains()
                .create("loja.com.br")
                .unwrap();
        final EmailDetail detail = client(FakeTransport.replying(200, Fixtures.body("emails.get")))
                .emails()
                .get("e")
                .unwrap();

        assertThat(created.createdAt()).isEmpty();
        assertThat(created.dkimRecordsToPublish().get(0).isInfrastructure()).isEmpty();
        assertThat(detail.cc()).isEmpty();
        assertThat(detail.apiKey()).isEmpty();
        assertThat(detail.subject()).contains("Pedido confirmado");
    }

    @Test
    void warmupNotStartedIsAnEmptyOptional() {
        final CoffeeMailResponse<Optional<DomainWarmupStatus>> response =
                client(FakeTransport.replying(200, "null")).domains().getWarmupStatus("d");

        assertThat(response.unwrap()).isEmpty();
    }
}
