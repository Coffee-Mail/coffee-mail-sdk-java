package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
import br.com.coffeemail.models.senders.CreateSenderInput;
import br.com.coffeemail.models.senders.ListSendersResponse;
import br.com.coffeemail.models.senders.SenderDetail;
import br.com.coffeemail.models.senders.VerifySenderResult;
import br.com.coffeemail.resources.Senders;
import br.com.coffeemail.resources.SendersAsync;

public final class DefaultSenders implements Senders {

    private final SendersAsync async;

    public DefaultSenders(final SendersAsync async) {
        this.async = async;
    }

    @Override
    public CoffeeMailResponse<SenderDetail> create(final CreateSenderInput input) {
        return async.create(input).join();
    }

    @Override
    public CoffeeMailResponse<ListSendersResponse> list() {
        return async.list().join();
    }

    @Override
    public CoffeeMailResponse<VerifySenderResult> verify(final String token) {
        return async.verify(token).join();
    }

    @Override
    public CoffeeMailResponse<NoContent> resendVerification(final String id) {
        return async.resendVerification(id).join();
    }

    @Override
    public CoffeeMailResponse<NoContent> delete(final String id) {
        return async.delete(id).join();
    }
}
