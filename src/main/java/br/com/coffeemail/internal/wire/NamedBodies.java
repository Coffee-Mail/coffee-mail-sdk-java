package br.com.coffeemail.internal.wire;

import br.com.coffeemail.models.audiences.CreateContactInput;
import br.com.coffeemail.models.suppressions.BulkSuppressionEntry;
import br.com.coffeemail.models.webhooks.ToggleableWebhookStatus;
import java.util.List;

public final class NamedBodies {

    private NamedBodies() {}

    public record CreateDomainBody(String name) {}

    public record TrackingDomainBody(String host) {}

    public record VerifySenderBody(String token) {}

    public record ToggleWebhookBody(ToggleableWebhookStatus status) {}

    public record BulkContactsBody(List<CreateContactInput> contacts) {}

    public record BulkSuppressionsBody(List<BulkSuppressionEntry> suppressions) {}
}
