package br.com.coffeemail.examples.spring;

import br.com.coffeemail.webhooks.VerifyWebhookInput;
import br.com.coffeemail.webhooks.WebhookVerifier;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoffeeMailWebhookController {

    private final WebhookVerifier verifier = WebhookVerifier.builder().build();
    private final String secret = System.getenv().getOrDefault("COFFEEMAIL_WEBHOOK_SECRET", "");

    @PostMapping("/webhooks/coffeemail")
    public ResponseEntity<Void> receive(
            @RequestBody final String rawBody, @RequestHeader final Map<String, String> headers) {
        final boolean authentic = verifier.verify(VerifyWebhookInput.fromHeaders(rawBody, headers, secret));
        if (!authentic) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.noContent().build();
    }
}
