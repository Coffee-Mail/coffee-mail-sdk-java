package br.com.coffeemail.internal.result;

public final class TryCatch {

    private TryCatch() {}

    public static <T> Result<T, Exception> of(final ThrowingSupplier<T> supplier) {
        try {
            return Result.ok(supplier.get());
        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();
            return Result.err(exception);
        } catch (final Exception exception) {
            return Result.err(exception);
        }
    }
}
