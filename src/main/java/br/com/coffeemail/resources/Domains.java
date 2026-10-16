package br.com.coffeemail.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.models.domains.CreateDomainResponse;
import br.com.coffeemail.models.domains.CustomHostStatus;
import br.com.coffeemail.models.domains.DeleteDomainResponse;
import br.com.coffeemail.models.domains.DkimRotation;
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

    /**
     * Inicia a rotação da chave DKIM. A chave em uso só muda depois que o registro TXT do novo seletor é
     * encontrado no DNS. Chamar de novo com uma rotação pendente devolve a mesma rotação.
     *
     * @param id identificador do domínio
     * @return a rotação pendente e o registro TXT a publicar; falha com {@code DOMAIN_NOT_FOUND} (404) ou
     *     {@code DOMAIN_NOT_VERIFIED} (409) em {@link br.com.coffeemail.errors.CoffeeMailException#apiCode()}
     */
    CoffeeMailResponse<DkimRotation> rotateDkim(String id);

    /**
     * Cadastra ou troca o host de tracking próprio, usado nos links de abertura e clique.
     *
     * @param id identificador do domínio
     * @param host subdomínio do domínio de envio, como {@code links.empresa.com.br}
     * @return o host e o registro CNAME a publicar; falha com {@code INVALID_TRACKING_HOST} (400),
     *     {@code TRACKING_DOMAINS_DISABLED} (409) ou 404 em
     *     {@link br.com.coffeemail.errors.CoffeeMailException#apiCode()}
     */
    CoffeeMailResponse<CustomHostStatus> setTrackingDomain(String id, String host);

    /**
     * Remove o host de tracking próprio; os links voltam a usar o host padrão da plataforma.
     *
     * @param id identificador do domínio
     * @return o host removido, ou vazio quando não havia host cadastrado
     */
    CoffeeMailResponse<Optional<CustomHostStatus>> removeTrackingDomain(String id);
}
