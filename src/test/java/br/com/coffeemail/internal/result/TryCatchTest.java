package br.com.coffeemail.internal.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.jupiter.api.Test;

final class TryCatchTest {

    private static <T, E> Optional<E> errorOf(final Result<T, E> result) {
        return result.fold(value -> Optional.empty(), Optional::of);
    }

    private static <T, E> Optional<T> valueOf(final Result<T, E> result) {
        return result.fold(Optional::of, error -> Optional.empty());
    }

    @Test
    void successfulSupplierBecomesOk() {
        assertThat(valueOf(TryCatch.of(() -> 42))).contains(42);
    }

    @Test
    void checkedExceptionBecomesErr() {
        final Result<String, Exception> result =
                TryCatch.of(() -> Files.readString(Path.of("/caminho/que/nao/existe.json")));

        assertThat(errorOf(result)).containsInstanceOf(NoSuchFileException.class);
    }

    @Test
    void interruptionRestoresTheInterruptFlag() {
        final Result<Integer, Exception> result = TryCatch.of(() -> {
            Thread.currentThread().interrupt();
            Thread.sleep(10);
            return 1;
        });

        assertThat(errorOf(result)).containsInstanceOf(InterruptedException.class);
        assertThat(Thread.interrupted()).isTrue();
    }

    @Test
    void mapAndFlatMapComposeOnlyOverOk() {
        final Result<Integer, String> ok = Result.<Integer, String>ok(2).map(value -> value * 3);
        final Result<Integer, String> chained = ok.flatMap(value -> Result.err("parou em " + value));
        final Result<Integer, String> failed =
                Result.<Integer, String>err("falhou").map(value -> value * 3);

        assertThat(valueOf(ok)).contains(6);
        assertThat(errorOf(chained)).contains("parou em 6");
        assertThat(errorOf(failed)).contains("falhou");
    }
}
