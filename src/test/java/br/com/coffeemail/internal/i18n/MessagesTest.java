package br.com.coffeemail.internal.i18n;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.coffeemail.CoffeeMailLocale;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

final class MessagesTest {

    @ParameterizedTest
    @EnumSource(CoffeeMailLocale.class)
    void everyKeyHasATranslationInEveryLocale(final CoffeeMailLocale locale) {
        assertThat(MessageKey.values()).allSatisfy(key -> {
            final String message = Messages.format(locale, key);
            assertThat(message).isNotEqualTo(key.name());
            assertThat(message).isNotBlank();
        });
    }

    @Test
    void placeholdersAreInterpolated() {
        assertThat(Messages.format(CoffeeMailLocale.EN, MessageKey.REQUEST_TIMEOUT, Map.of("timeoutMs", "250")))
                .isEqualTo("The request timed out after 250ms.");
    }

    @Test
    void unknownPlaceholdersAreLeftUntouched() {
        assertThat(Messages.format(CoffeeMailLocale.PT_BR, MessageKey.NETWORK_ERROR, Map.of("other", "x")))
                .endsWith("{details}");
    }
}
