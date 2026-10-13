package br.com.coffeemail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.coffeemail.errors.ApiErrorDetails;
import br.com.coffeemail.errors.CoffeeMailException;
import br.com.coffeemail.errors.NotFoundException;
import org.junit.jupiter.api.Test;

final class CoffeeMailResponseTest {

    private static final CoffeeMailException NOT_FOUND = new NotFoundException(ApiErrorDetails.of("não encontrado"));

    @Test
    void successExposesDataAndNoError() {
        final CoffeeMailResponse<String> response = CoffeeMailResponse.success("ok");

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.data()).contains("ok");
        assertThat(response.error()).isEmpty();
        assertThat(response.unwrap()).isEqualTo("ok");
        assertThat(response.fold(String::length, error -> -1)).isEqualTo(2);
    }

    @Test
    void failureExposesErrorAndNoData() {
        final CoffeeMailResponse<String> response = CoffeeMailResponse.failure(NOT_FOUND);

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.data()).isEmpty();
        assertThat(response.error()).contains(NOT_FOUND);
        assertThat(response.fold(String::length, error -> -1)).isEqualTo(-1);
    }

    @Test
    void unwrapOnFailureThrowsTheOriginalError() {
        final CoffeeMailResponse<String> response = CoffeeMailResponse.failure(NOT_FOUND);

        assertThatThrownBy(response::unwrap).isSameAs(NOT_FOUND);
    }

    @Test
    void mapTransformsSuccessAndPreservesFailure() {
        final CoffeeMailResponse<Integer> mapped =
                CoffeeMailResponse.success("abc").map(String::length);
        final CoffeeMailResponse<Integer> failed =
                CoffeeMailResponse.<String>failure(NOT_FOUND).map(String::length);

        assertThat(mapped.unwrap()).isEqualTo(3);
        assertThat(failed.error()).contains(NOT_FOUND);
    }

    @Test
    void failureCanBePatternMatched() {
        final CoffeeMailResponse<String> response = CoffeeMailResponse.failure(NOT_FOUND);

        assertThat(response).isInstanceOf(CoffeeMailResponse.Failure.class);
        assertThat(((CoffeeMailResponse.Failure<String>) response).exception()).isSameAs(NOT_FOUND);
    }
}
