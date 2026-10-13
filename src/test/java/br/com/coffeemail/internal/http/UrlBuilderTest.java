package br.com.coffeemail.internal.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

final class UrlBuilderTest {

    @Test
    void CA22_pathParametersAreEncoded() {
        assertThat(UrlBuilder.expand("/v1/product/emails/{id}/events", "a b/c?d"))
                .isEqualTo("/v1/product/emails/a%20b%2Fc%3Fd/events");
    }

    @Test
    void multiplePlaceholdersAreFilledInOrder() {
        assertThat(UrlBuilder.expand("/audiences/{id}/contacts/{contactId}", "aud_1", "ct_2"))
                .isEqualTo("/audiences/aud_1/contacts/ct_2");
    }

    @Test
    void queryIsAppendedInOrderAndEncoded() {
        final Map<String, String> query = new LinkedHashMap<>();
        query.put("recipient", "joão+teste@gmail.com");
        query.put("limit", "10");

        assertThat(UrlBuilder.build(URI.create("https://api.x"), "/v1/emails", query))
                .hasToString("https://api.x/v1/emails?recipient=jo%C3%A3o%2Bteste%40gmail.com&limit=10");
    }

    @Test
    void emptyQueryAddsNothing() {
        assertThat(UrlBuilder.build(URI.create("https://api.x"), "/v1/emails", Map.of()))
                .hasToString("https://api.x/v1/emails");
    }
}
