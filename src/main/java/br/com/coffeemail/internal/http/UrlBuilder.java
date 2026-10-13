package br.com.coffeemail.internal.http;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class UrlBuilder {

    private static final Pattern PLACEHOLDER = Pattern.compile("\\{[^}/]+}");
    private static final String ENCODED_SPACE = "%20";
    private static final String FORM_SPACE = "+";

    private UrlBuilder() {}

    public static String expand(final String template, final String... values) {
        final Iterator<String> remaining = Arrays.asList(values).iterator();
        return PLACEHOLDER.matcher(template).replaceAll(match -> Matcher.quoteReplacement(encode(remaining.next())));
    }

    public static URI build(final URI baseUrl, final String path, final Map<String, String> query) {
        return URI.create(baseUrl + path + queryString(query));
    }

    private static String queryString(final Map<String, String> query) {
        if (query.isEmpty()) {
            return "";
        }
        return query.entrySet().stream()
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&", "?", ""));
    }

    private static String encode(final String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace(FORM_SPACE, ENCODED_SPACE);
    }
}
