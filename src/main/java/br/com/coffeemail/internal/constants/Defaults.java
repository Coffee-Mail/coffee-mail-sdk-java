package br.com.coffeemail.internal.constants;

import java.net.URI;
import java.time.Duration;

public final class Defaults {

    public static final URI BASE_URL = URI.create("https://api.coffeemail.com.br");
    public static final Duration TIMEOUT = Duration.ofSeconds(10);
    public static final Duration INTROSPECTION_TTL = Duration.ofMinutes(5);
    public static final String API_KEY_ENVIRONMENT_VARIABLE = "COFFEEMAIL_API_KEY";
    public static final String USER_AGENT = "coffeemail-java/" + SdkVersion.VALUE;
    public static final String JSON_MEDIA_TYPE = "application/json";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String SANDBOX_ENABLED = "true";

    private Defaults() {}
}
