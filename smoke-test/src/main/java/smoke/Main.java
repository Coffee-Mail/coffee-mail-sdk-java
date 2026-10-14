package smoke;

import br.com.coffeemail.CoffeeMail;
import br.com.coffeemail.CoffeeMailAsync;
import br.com.coffeemail.errors.CoffeeMailException;
import br.com.coffeemail.errors.NotFoundException;
import br.com.coffeemail.models.domains.DomainDetail;
import br.com.coffeemail.models.domains.SendingIdentityState;
import br.com.coffeemail.models.emails.SendEmailInput;
import br.com.coffeemail.models.emails.SendEmailResponse;
import br.com.coffeemail.models.templates.UpdateTemplateInput;
import br.com.coffeemail.transport.HttpRequest;
import br.com.coffeemail.transport.HttpResponse;
import br.com.coffeemail.transport.HttpTransport;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Main {

    private static final String SEND = "{\"id\":\"eml_1\",\"status\":\"queued\",\"queuedAt\":\"2026-10-14T12:00:00Z\"}";
    private static final String DOMAIN = "{\"id\":\"dom_1\",\"name\":\"loja.com.br\",\"status\":\"verified\","
            + "\"statusReason\":null,\"sendingIdentity\":{\"provider\":\"aws_ses\",\"state\":\"ready\",\"lastSyncedAt\":null},"
            + "\"dkimSelector\":\"cm1\",\"dkimPublicKey\":null,\"verifiedAt\":null,\"lastCheckAt\":null,"
            + "\"createdAt\":\"2026-10-14T12:00:00Z\",\"dnsRecordsToPublish\":[{\"type\":\"TXT\",\"host\":\"h\",\"value\":\"v\"}]}";
    private static final String NOT_FOUND =
            "{\"error\":{\"code\":\"TEMPLATE_NOT_FOUND\",\"message\":\"Template não encontrado\",\"meta\":{\"id\":\"tpl_1\"}}}";

    private Main() {}

    public static void main(final String[] args) {
        final List<HttpRequest> requests = new CopyOnWriteArrayList<>();
        final HttpTransport transport = (request, timeout) -> {
            requests.add(request);
            return CompletableFuture.completedFuture(respond(request));
        };
        final CoffeeMail client = CoffeeMail.builder().apiKey("cm_live_smoke").transport(transport).build();
        final CoffeeMailAsync asyncClient =
                CoffeeMailAsync.builder().apiKey("cm_live_smoke").transport(transport).build();

        final SendEmailResponse sent = client.emails()
                .send(SendEmailInput.builder("Loja <a@loja.com.br>", "b@gmail.com").subject("Oi").build())
                .unwrap();
        final DomainDetail domain = asyncClient.domains().get("dom_1").join().unwrap();
        final CoffeeMailException missing = client.templates()
                .update("tpl_1", UpdateTemplateInput.builder().clearAlias().build())
                .error()
                .orElseThrow();
        final String body = requests.get(0).body().orElseThrow();

        check(sent.id().equals("eml_1"), "envio decodificado");
        check(body.contains("\"to\":[{\"email\":\"b@gmail.com\"}]"), "corpo serializado");
        check(domain.sendingIdentity().map(identity -> identity.state() == SendingIdentityState.READY).orElse(false),
                "domínio decodificado");
        check(missing instanceof NotFoundException, "erro tipado");
        check("tpl_1".equals(missing.details().get("id")), "metadados do erro");
        check(requests.get(2).body().orElseThrow().equals("{\"alias\":null}"), "patch com null explícito");
        System.out.println("SMOKE OK (" + Main.class.getModule() + ")");
    }

    private static HttpResponse respond(final HttpRequest request) {
        final String path = request.uri().getPath();
        if (path.endsWith("/emails")) {
            return new HttpResponse(202, Map.of(), SEND);
        }
        if (path.contains("/domains/")) {
            return new HttpResponse(200, Map.of(), DOMAIN);
        }
        return new HttpResponse(404, Map.of(), NOT_FOUND);
    }

    private static void check(final boolean condition, final String description) {
        if (!condition) {
            System.err.println("SMOKE FALHOU: " + description);
            System.exit(1);
        }
    }
}
