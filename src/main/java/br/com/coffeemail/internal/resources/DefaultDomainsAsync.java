package br.com.coffeemail.internal.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.internal.ClientRuntime;
import br.com.coffeemail.internal.constants.ApiRoutes;
import br.com.coffeemail.internal.http.Operation;
import br.com.coffeemail.internal.http.RequestExecutor;
import br.com.coffeemail.internal.http.UrlBuilder;
import br.com.coffeemail.internal.json.ResponseType;
import br.com.coffeemail.internal.wire.NamedBodies.CreateDomainBody;
import br.com.coffeemail.internal.wire.NamedBodies.TrackingDomainBody;
import br.com.coffeemail.models.domains.CreateDomainResponse;
import br.com.coffeemail.models.domains.CustomHostStatus;
import br.com.coffeemail.models.domains.DeleteDomainResponse;
import br.com.coffeemail.models.domains.DkimRotation;
import br.com.coffeemail.models.domains.DomainDetail;
import br.com.coffeemail.models.domains.DomainHealthResponse;
import br.com.coffeemail.models.domains.DomainWarmupStatus;
import br.com.coffeemail.models.domains.ListDomainsResponse;
import br.com.coffeemail.models.domains.VerifyDomainResponse;
import br.com.coffeemail.resources.DomainsAsync;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class DefaultDomainsAsync implements DomainsAsync {

    private final RequestExecutor executor;

    public DefaultDomainsAsync(final ClientRuntime runtime) {
        this.executor = runtime.executor();
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<CreateDomainResponse>> create(final String name) {
        return executor.executeAsync(Operation.post(ApiRoutes.DOMAINS, ResponseType.of(CreateDomainResponse.class))
                .withBody(new CreateDomainBody(name)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<ListDomainsResponse>> list() {
        return executor.executeAsync(Operation.get(ApiRoutes.DOMAINS, ResponseType.of(ListDomainsResponse.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<DomainDetail>> get(final String id) {
        return executor.executeAsync(
                Operation.get(UrlBuilder.expand(ApiRoutes.DOMAIN, id), ResponseType.of(DomainDetail.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<VerifyDomainResponse>> verify(final String id) {
        return executor.executeAsync(Operation.post(
                UrlBuilder.expand(ApiRoutes.DOMAIN_VERIFY, id), ResponseType.of(VerifyDomainResponse.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<DeleteDomainResponse>> delete(final String id) {
        return executor.executeAsync(
                Operation.delete(UrlBuilder.expand(ApiRoutes.DOMAIN, id), ResponseType.of(DeleteDomainResponse.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<DomainHealthResponse>> getHealth(final String id) {
        return executor.executeAsync(Operation.post(
                UrlBuilder.expand(ApiRoutes.DOMAIN_HEALTH, id), ResponseType.of(DomainHealthResponse.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<Optional<DomainWarmupStatus>>> getWarmupStatus(final String id) {
        return executor.executeAsync(Operation.get(
                UrlBuilder.expand(ApiRoutes.DOMAIN_WARMUP, id), ResponseType.optionalOf(DomainWarmupStatus.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<DkimRotation>> rotateDkim(final String id) {
        return executor.executeAsync(Operation.post(
                UrlBuilder.expand(ApiRoutes.DOMAIN_DKIM_ROTATE, id), ResponseType.of(DkimRotation.class)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<CustomHostStatus>> setTrackingDomain(
            final String id, final String host) {
        return executor.executeAsync(Operation.put(
                        UrlBuilder.expand(ApiRoutes.DOMAIN_TRACKING_DOMAIN, id),
                        ResponseType.of(CustomHostStatus.class))
                .withBody(new TrackingDomainBody(host)));
    }

    @Override
    public CompletableFuture<CoffeeMailResponse<Optional<CustomHostStatus>>> removeTrackingDomain(final String id) {
        return executor.executeAsync(Operation.delete(
                UrlBuilder.expand(ApiRoutes.DOMAIN_TRACKING_DOMAIN, id),
                ResponseType.optionalOf(CustomHostStatus.class)));
    }
}
