package br.com.coffeemail.models.emails;

import br.com.coffeemail.models.support.Copies;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Endereço de e-mail com nome de exibição opcional.
 *
 * @param email endereço, como {@code contato@empresa.com.br}
 * @param name nome de exibição, como {@code Equipe de Suporte}
 */
public record EmailAddress(String email, Optional<String> name) {

    private static final Pattern NAMED_EMAIL = Pattern.compile("^(?:(?<name>.*?)\\s*<)?(?<email>[^<>\\s]+)>?$");
    private static final Pattern SURROUNDING_QUOTES = Pattern.compile("^[\"']|[\"']$");
    private static final String NAME_GROUP = "name";
    private static final String EMAIL_GROUP = "email";

    /**
     * Interpreta um endereço simples ({@code a@b.com}) ou no formato RFC 5322 ({@code Nome <a@b.com>}).
     *
     * @param value o endereço em texto
     * @return o endereço estruturado
     */
    public static EmailAddress parse(final String value) {
        final String trimmed = value.strip();
        final Matcher matcher = NAMED_EMAIL.matcher(trimmed);
        if (!matcher.matches()) {
            return new EmailAddress(trimmed, Optional.empty());
        }
        final Optional<String> name = Optional.ofNullable(matcher.group(NAME_GROUP))
                .map(raw -> SURROUNDING_QUOTES.matcher(raw.strip()).replaceAll(""));
        return new EmailAddress(Optional.ofNullable(matcher.group(EMAIL_GROUP)).orElse(trimmed), name);
    }

    /**
     * Cria um endereço com nome de exibição.
     *
     * @param email o endereço
     * @param name o nome de exibição
     * @return o endereço estruturado
     */
    public static EmailAddress of(final String email, final String name) {
        return new EmailAddress(email, Optional.of(name));
    }

    /**
     * Remove espaços nas pontas e descarta nomes vazios.
     *
     * @param email endereço
     * @param name nome de exibição
     */
    public EmailAddress {
        email = email.strip();
        name = Copies.optional(name).map(String::strip).filter(text -> !text.isEmpty());
    }
}
