package br.com.coffeemail.resources;

import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.NoContent;
import br.com.coffeemail.models.senders.CreateSenderInput;
import br.com.coffeemail.models.senders.ListSendersResponse;
import br.com.coffeemail.models.senders.SenderDetail;
import br.com.coffeemail.models.senders.VerifySenderResult;

/** Remetentes verificados por endereço, alternativa ao domínio completo (síncrono). */
public interface Senders {

    /**
     * Cadastra um remetente e dispara o e-mail de verificação.
     *
     * @param input endereço e nome de exibição
     * @return o remetente criado
     */
    CoffeeMailResponse<SenderDetail> create(CreateSenderInput input);

    /**
     * Lista os remetentes da organização.
     *
     * @return os remetentes
     */
    CoffeeMailResponse<ListSendersResponse> list();

    /**
     * Confirma um remetente com o token recebido por e-mail.
     *
     * @param token token de verificação
     * @return o remetente confirmado
     */
    CoffeeMailResponse<VerifySenderResult> verify(String token);

    /**
     * Reenvia o e-mail de verificação com um token novo (o anterior expira).
     *
     * @param id identificador do remetente
     * @return {@link NoContent} em caso de sucesso
     */
    CoffeeMailResponse<NoContent> resendVerification(String id);

    /**
     * Remove um remetente.
     *
     * @param id identificador do remetente
     * @return {@link NoContent} em caso de sucesso
     */
    CoffeeMailResponse<NoContent> delete(String id);
}
