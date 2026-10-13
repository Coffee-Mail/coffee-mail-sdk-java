package br.com.coffeemail.internal.result;

@FunctionalInterface
public interface ThrowingSupplier<T> {
    T get() throws Exception;
}
