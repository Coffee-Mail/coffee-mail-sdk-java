package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.internal.ClientRuntime;
import br.com.coffeemail.internal.constants.ApiRoutes;
import br.com.coffeemail.internal.http.Operation;
import br.com.coffeemail.internal.http.RequestExecutor;
import br.com.coffeemail.internal.json.ResponseType;
import br.com.coffeemail.internal.wire.NamedBodies.ValidateEmailsBody;
import br.com.coffeemail.models.validation.ValidateEmailsResponse;
import br.com.coffeemail.resources.ValidationAsync;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class DefaultValidationAsync implements ValidationAsync {

    private final RequestExecutor executor;

    public DefaultValidationAsync(final ClientRuntime runtime) {
        this.executor = runtime.executor();
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ValidateEmailsResponse>> validate(final List<String> emails) {
        return executor.executeAsync(Operation.post(ApiRoutes.VALIDATE, ResponseType.of(ValidateEmailsResponse.class))
                .withBody(new ValidateEmailsBody(List.copyOf(emails))));
    }
}
