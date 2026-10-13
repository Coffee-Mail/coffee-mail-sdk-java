package br.com.coffeemail.internal.i18n;

import br.com.coffeemail.CoffeeMailLocale;
import java.util.Map;
import java.util.Optional;

public final class Messages {

    private static final Map<MessageKey, String> PT_BR = Map.of(
            MessageKey.MISSING_API_KEY,
            "A chave de API não foi informada. Forneça uma chave válida (ex: cm_live_...) no builder do CoffeeMail"
                    + " ou na variável de ambiente COFFEEMAIL_API_KEY.",
            MessageKey.REQUEST_TIMEOUT,
            "A requisição excedeu o tempo limite configurado de {timeoutMs}ms.",
            MessageKey.NETWORK_ERROR,
            "Falha de rede ao tentar se comunicar com os servidores do CoffeeMail: {details}",
            MessageKey.UNEXPECTED_ERROR,
            "Ocorreu um erro inesperado durante a comunicação com a API do CoffeeMail.",
            MessageKey.INVALID_JSON,
            "A resposta do servidor não pôde ser interpretada como JSON válido.",
            MessageKey.RATE_LIMIT_EXCEEDED,
            "Limite de requisições excedido (Rate Limit). Tente novamente em alguns segundos.",
            MessageKey.PERMISSION_DENIED,
            "Esta chave de API não possui a permissão necessária ({requiredPermission}) para executar esta"
                    + " operação. Crie uma chave com permissão 'full_access' no dashboard.");

    private static final Map<MessageKey, String> EN = Map.of(
            MessageKey.MISSING_API_KEY,
            "API key was not provided. Pass a valid API key (e.g. cm_live_...) to the CoffeeMail builder"
                    + " or set the COFFEEMAIL_API_KEY environment variable.",
            MessageKey.REQUEST_TIMEOUT,
            "The request timed out after {timeoutMs}ms.",
            MessageKey.NETWORK_ERROR,
            "Network error while attempting to communicate with CoffeeMail API: {details}",
            MessageKey.UNEXPECTED_ERROR,
            "An unexpected error occurred while communicating with CoffeeMail API.",
            MessageKey.INVALID_JSON,
            "The server response could not be parsed as valid JSON.",
            MessageKey.RATE_LIMIT_EXCEEDED,
            "Rate limit exceeded. Please retry in a few moments.",
            MessageKey.PERMISSION_DENIED,
            "This API key lacks the required permission ({requiredPermission}) for this operation."
                    + " Create a key with 'full_access' permission in the dashboard.");

    private static final Map<CoffeeMailLocale, Map<MessageKey, String>> CATALOG =
            Map.of(CoffeeMailLocale.PT_BR, PT_BR, CoffeeMailLocale.EN, EN);

    private Messages() {}

    public static String format(final CoffeeMailLocale locale, final MessageKey key) {
        return format(locale, key, Map.of());
    }

    public static String format(final CoffeeMailLocale locale, final MessageKey key, final Map<String, String> params) {
        final String template = Optional.ofNullable(CATALOG.get(locale))
                .map(messages -> messages.get(key))
                .orElse(key.name());
        return params.entrySet().stream()
                .reduce(
                        template,
                        (text, param) -> text.replace("{" + param.getKey() + "}", param.getValue()),
                        (left, right) -> right);
    }
}
