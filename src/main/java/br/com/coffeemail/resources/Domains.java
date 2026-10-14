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

/** Cadastro e verificação de domínios de envio (síncrono). */
public interface Domains {

    /**
     * Cadastra um domínio e gera os registros DNS a publicar.
     *
     * @param name domínio, como {@code empresa.com.br}
     * @return o domínio criado e os registros DKIM
     */
    CoffeeMailResponse<CreateDomainResponse> create(String name);

    /**
     * Lista os domínios da organização.
     *
     * @return os domínios
     */
    CoffeeMailResponse<ListDomainsResponse> list();

    /**
     * Busca um domínio com os registros DNS pendentes.
     *
     * @param id identificador do domínio
     * @return o domínio
     */
    CoffeeMailResponse<DomainDetail> get(String id);

    /**
     * Dispara a verificação dos registros DNS.
     *
     * @param id identificador do domínio
     * @return o resultado de cada verificação
     */
    CoffeeMailResponse<VerifyDomainResponse> verify(String id);

    /**
     * Remove um domínio.
     *
     * @param id identificador do domínio
     * @return a confirmação
     */
    CoffeeMailResponse<DeleteDomainResponse> delete(String id);

    /**
     * Executa um diagnóstico de SPF, DKIM e DMARC.
     *
     * @param id identificador do domínio
     * @return o diagnóstico
     */
    CoffeeMailResponse<DomainHealthResponse> getHealth(String id);

    /**
     * Consulta o progresso do aquecimento do domínio.
     *
     * @param id identificador do domínio
     * @return o progresso, ou vazio quando o aquecimento não começou
     */
    CoffeeMailResponse<Optional<DomainWarmupStatus>> getWarmupStatus(String id);
}
