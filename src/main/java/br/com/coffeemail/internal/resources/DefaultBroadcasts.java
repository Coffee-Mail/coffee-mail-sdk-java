package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.models.broadcasts.BroadcastDetail;
import br.com.coffeemail.models.broadcasts.CancelBroadcastResult;
import br.com.coffeemail.models.broadcasts.CreateBroadcastInput;
import br.com.coffeemail.models.broadcasts.ListBroadcastsQuery;
import br.com.coffeemail.models.broadcasts.ListBroadcastsResponse;
import br.com.coffeemail.models.broadcasts.SendBroadcastResult;
import br.com.coffeemail.models.broadcasts.TestSendBroadcastInput;
import br.com.coffeemail.models.broadcasts.TestSendBroadcastResult;
import br.com.coffeemail.resources.Broadcasts;
import br.com.coffeemail.resources.BroadcastsAsync;

public final class DefaultBroadcasts implements Broadcasts {

    private final BroadcastsAsync async;

    public DefaultBroadcasts(final BroadcastsAsync async) {
        this.async = async;
    }

    @Override
    public CoffeeMailResponse<BroadcastDetail> create(final CreateBroadcastInput input) {
        return async.create(input).join();
    }

    @Override
    public CoffeeMailResponse<ListBroadcastsResponse> list() {
        return async.list().join();
    }

    @Override
    public CoffeeMailResponse<ListBroadcastsResponse> list(final ListBroadcastsQuery query) {
        return async.list(query).join();
    }

    @Override
    public CoffeeMailResponse<BroadcastDetail> get(final String id) {
        return async.get(id).join();
    }

    @Override
    public CoffeeMailResponse<SendBroadcastResult> send(final String id) {
        return async.send(id).join();
    }

    @Override
    public CoffeeMailResponse<CancelBroadcastResult> cancel(final String id) {
        return async.cancel(id).join();
    }

    @Override
    public CoffeeMailResponse<TestSendBroadcastResult> testSend(final String id, final TestSendBroadcastInput input) {
        return async.testSend(id, input).join();
    }
}
