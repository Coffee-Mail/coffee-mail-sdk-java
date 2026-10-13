package br.com.coffeemail.internal.result;

import java.util.function.Function;

public sealed interface Result<T, E> permits Result.Ok, Result.Err {

    static <T, E> Result<T, E> ok(final T value) {
        return new Ok<>(value);
    }

    static <T, E> Result<T, E> err(final E error) {
        return new Err<>(error);
    }

    <R> R fold(Function<? super T, ? extends R> onOk, Function<? super E, ? extends R> onErr);

    default <R> Result<R, E> map(final Function<? super T, ? extends R> mapper) {
        return fold(value -> Result.<R, E>ok(mapper.apply(value)), Result::<R, E>err);
    }

    default <R> Result<R, E> flatMap(final Function<? super T, Result<R, E>> mapper) {
        return fold(mapper, Result::<R, E>err);
    }

    record Ok<T, E>(T value) implements Result<T, E> {
        @Override
        public <R> R fold(final Function<? super T, ? extends R> onOk, final Function<? super E, ? extends R> onErr) {
            return onOk.apply(value);
        }
    }

    record Err<T, E>(E error) implements Result<T, E> {
        @Override
        public <R> R fold(final Function<? super T, ? extends R> onOk, final Function<? super E, ? extends R> onErr) {
            return onErr.apply(error);
        }
    }
}
