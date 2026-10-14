package br.com.coffeemail.internal.wire;

import br.com.coffeemail.internal.constants.Defaults;
import br.com.coffeemail.internal.constants.HeaderNames;
import br.com.coffeemail.models.emails.SendEmailInput;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SendEmailHeaders {

    private SendEmailHeaders() {}

    public static Map<String, String> of(final SendEmailInput input) {
        return entries(input).collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map<String, String> merged(final List<SendEmailInput> inputs) {
        return inputs.stream()
                .flatMap(SendEmailHeaders::entries)
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey, Map.Entry::getValue, (first, second) -> second));
    }

    private static Stream<Map.Entry<String, String>> entries(final SendEmailInput input) {
        final String idempotencyKey = input.idempotencyKey();
        final Stream<Map.Entry<String, String>> idempotency = idempotencyKey == null || idempotencyKey.isBlank()
                ? Stream.empty()
                : Stream.of(Map.entry(HeaderNames.IDEMPOTENCY_KEY, idempotencyKey));
        final Stream<Map.Entry<String, String>> sandbox =
                input.sandbox() ? Stream.of(Map.entry(HeaderNames.SANDBOX, Defaults.SANDBOX_ENABLED)) : Stream.empty();
        return Stream.concat(idempotency, sandbox);
    }
}
