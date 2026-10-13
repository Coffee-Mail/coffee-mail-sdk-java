package br.com.coffeemail;

import br.com.coffeemail.errors.CoffeeMailException;
import java.util.Optional;
import java.util.function.Function;

/**
 * Envelope de retorno de todas as operações do SDK: contém o dado ou o erro, nunca os dois. Nenhuma chamada de API
 * lança exceção; use {@link #unwrap()} quando preferir esse estilo.
 *
 * @param <T> tipo do dado em caso de sucesso
 */
public sealed interface CoffeeMailResponse<T> permits CoffeeMailResponse.Success, CoffeeMailResponse.Failure {

    /**
     * Cria uma resposta de sucesso.
     *
     * @param data o dado retornado
     * @param <T> tipo do dado
     * @return a resposta de sucesso
     */
    static <T> CoffeeMailResponse<T> success(final T data) {
        return new Success<>(data);
    }

    /**
     * Cria uma resposta de falha.
     *
     * @param error o erro ocorrido
     * @param <T> tipo do dado esperado
     * @return a resposta de falha
     */
    static <T> CoffeeMailResponse<T> failure(final CoffeeMailException error) {
        return new Failure<>(error);
    }

    /**
     * Indica se a operação teve sucesso.
     *
     * @return {@code true} quando há dado e nenhum erro
     */
    boolean isSuccess();

    /**
     * O dado retornado pela operação.
     *
     * @return o dado, ou vazio em caso de falha
     */
    Optional<T> data();

    /**
     * O erro ocorrido na operação.
     *
     * @return o erro, ou vazio em caso de sucesso
     */
    Optional<CoffeeMailException> error();

    /**
     * Retorna o dado ou lança o erro.
     *
     * @return o dado
     * @throws CoffeeMailException quando a operação falhou
     */
    T unwrap();

    /**
     * Reduz a resposta a um único valor.
     *
     * @param onSuccess função aplicada ao dado
     * @param onFailure função aplicada ao erro
     * @param <R> tipo do resultado
     * @return o resultado da função aplicada
     */
    <R> R fold(
            Function<? super T, ? extends R> onSuccess, Function<? super CoffeeMailException, ? extends R> onFailure);

    /**
     * Transforma o dado de uma resposta de sucesso, preservando a falha.
     *
     * @param mapper função de transformação
     * @param <R> tipo do novo dado
     * @return a resposta transformada
     */
    default <R> CoffeeMailResponse<R> map(final Function<? super T, ? extends R> mapper) {
        return fold(data -> success(mapper.apply(data)), CoffeeMailResponse::failure);
    }

    /**
     * Resposta de sucesso.
     *
     * @param value o dado retornado
     * @param <T> tipo do dado
     */
    record Success<T>(T value) implements CoffeeMailResponse<T> {

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public Optional<T> data() {
            return Optional.of(value);
        }

        @Override
        public Optional<CoffeeMailException> error() {
            return Optional.empty();
        }

        @Override
        public T unwrap() {
            return value;
        }

        @Override
        public <R> R fold(
                final Function<? super T, ? extends R> onSuccess,
                final Function<? super CoffeeMailException, ? extends R> onFailure) {
            return onSuccess.apply(value);
        }
    }

    /**
     * Resposta de falha.
     *
     * @param exception o erro ocorrido
     * @param <T> tipo do dado esperado
     */
    record Failure<T>(CoffeeMailException exception) implements CoffeeMailResponse<T> {

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public Optional<T> data() {
            return Optional.empty();
        }

        @Override
        public Optional<CoffeeMailException> error() {
            return Optional.of(exception);
        }

        @Override
        public T unwrap() {
            throw exception;
        }

        @Override
        public <R> R fold(
                final Function<? super T, ? extends R> onSuccess,
                final Function<? super CoffeeMailException, ? extends R> onFailure) {
            return onFailure.apply(exception);
        }
    }
}
