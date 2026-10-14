package br.com.coffeemail.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.models.domains.CreateDomainResponse;
import br.com.coffeemail.models.domains.DeleteDomainResponse;
import br.com.coffeemail.models.domains.DomainDetail;
import br.com.coffeemail.models.domains.DomainHealthResponse;
import br.com.coffeemail.models.domains.DomainWarmupStatus;
import br.com.coffeemail.models.domains.ListDomainsResponse;
import br.com.coffeemail.models.domains.VerifyDomainResponse;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/** Cadastro e verificação de domínios de envio (assíncrono). */
public interface DomainsAsync {

    /**
     * Cadastra um domínio e gera os registros DNS a publicar.
     *
     * @param name domínio, como {@code empresa.com.br}
     * @return o domínio criado e os registros DKIM
     */
    CompletableFuture<CoffeeMailResponse<CreateDomainResponse>> create(String name);

    /**
     * Lista os domínios da organização.
     *
     * @return os domínios
     */
    CompletableFuture<CoffeeMailResponse<ListDomainsResponse>> list();

    /**
     * Busca um domínio com os registros DNS pendentes.
     *
     * @param id identificador do domínio
     * @return o domínio
     */
    CompletableFuture<CoffeeMailResponse<DomainDetail>> get(String id);

    /**
     * Dispara a verificação dos registros DNS.
     *
     * @param id identificador do domínio
     * @return o resultado de cada verificação
     */
    CompletableFuture<CoffeeMailResponse<VerifyDomainResponse>> verify(String id);

    /**
     * Remove um domínio.
     *
     * @param id identificador do domínio
     * @return a confirmação
     */
    CompletableFuture<CoffeeMailResponse<DeleteDomainResponse>> delete(String id);

    /**
     * Executa um diagnóstico de SPF, DKIM e DMARC.
     *
     * @param id identificador do domínio
     * @return o diagnóstico
     */
    CompletableFuture<CoffeeMailResponse<DomainHealthResponse>> getHealth(String id);

    /**
     * Consulta o progresso do aquecimento do domínio.
     *
     * @param id identificador do domínio
     * @return o progresso, ou vazio quando o aquecimento não começou
     */
    CompletableFuture<CoffeeMailResponse<Optional<DomainWarmupStatus>>> getWarmupStatus(String id);
}
