package br.com.coffeemail.internal;

import br.com.coffeemail.CoffeeMailLocale;
import br.com.coffeemail.transport.HttpTransport;
import java.net.URI;
import java.time.Clock;
import java.time.Duration;

public record ClientConfig(
        String apiKey, URI baseUrl, CoffeeMailLocale locale, Duration timeout, HttpTransport transport, Clock clock) {}
