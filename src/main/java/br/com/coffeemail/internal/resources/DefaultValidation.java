package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.models.validation.ValidateEmailsResponse;
import br.com.coffeemail.resources.Validation;
import br.com.coffeemail.resources.ValidationAsync;
import java.util.List;

public final class DefaultValidation implements Validation {

    private final ValidationAsync async;

    public DefaultValidation(final ValidationAsync async) {
        this.async = async;
    }

    @Override
    public CoffeeMailResponse<ValidateEmailsResponse> validate(final List<String> emails) {
        return async.validate(emails).join();
    }
}
