package br.com.coffeemail.internal.constants;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

final class SdkVersionTest {

    private static final String EXPECTED_VERSION_PROPERTY = "coffeemail.expectedSdkVersion";

    @Test
    void sdkVersionMatchesTheBuildVersion() {
        assertThat(SdkVersion.VALUE).isEqualTo(System.getProperty(EXPECTED_VERSION_PROPERTY));
    }

    @Test
    void sdkVersionIsSemver() {
        assertThat(SdkVersion.VALUE).matches("\\d+\\.\\d+\\.\\d+(-[0-9A-Za-z.-]+)?");
    }
}
