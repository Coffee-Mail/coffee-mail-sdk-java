package br.com.coffeemail.transport;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * Porta de transporte HTTP do SDK. A implementação padrão usa o {@code java.net.http.HttpClient}; implemente esta
 * interface para usar um proxy, outro cliente HTTP ou dublês de teste.
 */
@FunctionalInterface
public interface HttpTransport {

    /**
     * Envia a requisição. Falhas de rede devem completar o futuro excepcionalmente.
     *
     * @param request requisição montada pelo SDK
     * @param timeout tempo limite configurado no cliente
     * @return o futuro com a resposta
     */
    CompletableFuture<HttpResponse> send(HttpRequest request, Duration timeout);
}
