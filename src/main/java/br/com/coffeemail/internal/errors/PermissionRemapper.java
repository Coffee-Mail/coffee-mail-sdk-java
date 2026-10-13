package br.com.coffeemail.internal.errors;

import br.com.coffeemail.CoffeeMailLocale;
import br.com.coffeemail.errors.ApiErrorDetails;
import br.com.coffeemail.errors.CoffeeMailException;
import br.com.coffeemail.errors.ForbiddenException;
import br.com.coffeemail.errors.PermissionException;
import br.com.coffeemail.internal.i18n.MessageKey;
import br.com.coffeemail.internal.i18n.Messages;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public final class PermissionRemapper {

    public static final String FULL_ACCESS = "full_access";

    private static final String OPERATION_DETAIL = "operation";
    private static final String REQUIRED_PERMISSION_PARAM = "requiredPermission";

    private PermissionRemapper() {}

    public static UnaryOperator<CoffeeMailException> requireFullAccess(
            final CoffeeMailLocale locale, final String operation) {
        return error -> error instanceof ForbiddenException ? toPermissionError(error, locale, operation) : error;
    }

    private static CoffeeMailException toPermissionError(
            final CoffeeMailException error, final CoffeeMailLocale locale, final String operation) {
        final Map<String, Object> details = new LinkedHashMap<>(error.details());
        details.put(OPERATION_DETAIL, operation);
        final String message = Messages.format(
                locale,
                MessageKey.PERMISSION_DENIED,
                Map.of(REQUIRED_PERMISSION_PARAM, FULL_ACCESS + " (called by " + operation + ")"));
        return new PermissionException(
                new ApiErrorDetails(
                        message,
                        error.apiCode().orElse(null),
                        details,
                        error.requestId().orElse(null)),
                FULL_ACCESS);
    }
}
