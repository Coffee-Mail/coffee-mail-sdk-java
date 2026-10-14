package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.models.domains.CreateDomainResponse;
import br.com.coffeemail.models.domains.DeleteDomainResponse;
import br.com.coffeemail.models.domains.DomainDetail;
import br.com.coffeemail.models.domains.DomainHealthResponse;
import br.com.coffeemail.models.domains.DomainWarmupStatus;
import br.com.coffeemail.models.domains.ListDomainsResponse;
import br.com.coffeemail.models.domains.VerifyDomainResponse;
import br.com.coffeemail.resources.Domains;
import br.com.coffeemail.resources.DomainsAsync;
import java.util.Optional;

public final class DefaultDomains implements Domains {

    private final DomainsAsync async;

    public DefaultDomains(final DomainsAsync async) {
        this.async = async;
    }

    @Override
    public CoffeeMailResponse<CreateDomainResponse> create(final String name) {
        return async.create(name).join();
    }

    @Override
    public CoffeeMailResponse<ListDomainsResponse> list() {
        return async.list().join();
    }

    @Override
    public CoffeeMailResponse<DomainDetail> get(final String id) {
        return async.get(id).join();
    }

    @Override
    public CoffeeMailResponse<VerifyDomainResponse> verify(final String id) {
        return async.verify(id).join();
    }

    @Override
    public CoffeeMailResponse<DeleteDomainResponse> delete(final String id) {
        return async.delete(id).join();
    }

    @Override
    public CoffeeMailResponse<DomainHealthResponse> getHealth(final String id) {
        return async.getHealth(id).join();
    }

    @Override
    public CoffeeMailResponse<Optional<DomainWarmupStatus>> getWarmupStatus(final String id) {
        return async.getWarmupStatus(id).join();
    }
}
