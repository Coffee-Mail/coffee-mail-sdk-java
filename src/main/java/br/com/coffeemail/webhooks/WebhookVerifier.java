package br.com.coffeemail.webhooks;

import br.com.coffeemail.internal.result.TryCatch;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Clock;
import java.time.Duration;
import java.util.HexFormat;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Verifica a autenticidade de entregas de webhook da CoffeeMail.
 *
 * <p>A assinatura é {@code sha256=} seguido do HMAC-SHA256 em hexadecimal de
 * {@code timestamp + "." + idempotencyKey + "." + corpoBruto}, comparada em tempo constante. Entregas com timestamp
 * fora da janela de tolerância (padrão de 5 minutos) são rejeitadas para impedir replay.
 */
public final class WebhookVerifier {

    /** Janela de tolerância padrão, igual à do servidor. */
    public static final Duration DEFAULT_TOLERANCE = Duration.ofMinutes(5);

    private static final String ALGORITHM = "HmacSHA256";
    private static final String SIGNATURE_PREFIX = "sha256=";
    private static final String SEPARATOR = ".";
    private static final Pattern UNIX_SECONDS = Pattern.compile("-?\\d{1,18}");
    private static final WebhookVerifier DEFAULT = builder().build();

    private final Duration tolerance;
    private final Clock clock;

    private WebhookVerifier(final Duration tolerance, final Clock clock) {
        this.tolerance = tolerance;
        this.clock = clock;
    }

    /**
     * Inicia a configuração de um verificador.
     *
     * @return o builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Verifica com a tolerância padrão e o relógio do sistema.
     *
     * @param input corpo bruto, cabeçalhos de assinatura e segredo
     * @return {@code true} quando a assinatura é válida e está dentro da janela de tolerância
     */
    public static boolean verifySignature(final VerifyWebhookInput input) {
        return DEFAULT.verify(input);
    }

    /**
     * Verifica uma entrega.
     *
     * @param input corpo bruto, cabeçalhos de assinatura e segredo
     * @return {@code true} quando a assinatura é válida e está dentro da janela de tolerância
     */
    public boolean verify(final VerifyWebhookInput input) {
        return withinTolerance(input.timestamp()) && signatureMatches(input);
    }

    private boolean withinTolerance(final String timestamp) {
        final long now = clock.instant().getEpochSecond();
        return Optional.of(timestamp.strip())
                .filter(UNIX_SECONDS.asMatchPredicate())
                .map(Long::parseLong)
                .filter(seconds -> Math.abs(now - seconds) <= tolerance.toSeconds())
                .isPresent();
    }

    private static boolean signatureMatches(final VerifyWebhookInput input) {
        final String signedContent =
                input.timestamp() + SEPARATOR + input.idempotencyKey() + SEPARATOR + input.payload();
        return TryCatch.of(() -> sign(input.secret(), signedContent))
                .fold(
                        expected -> MessageDigest.isEqual(
                                expected.getBytes(StandardCharsets.UTF_8),
                                input.signature().getBytes(StandardCharsets.UTF_8)),
                        failure -> false);
    }

    private static String sign(final String secret, final String content) throws Exception {
        final Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM));
        return SIGNATURE_PREFIX + HexFormat.of().formatHex(mac.doFinal(content.getBytes(StandardCharsets.UTF_8)));
    }

    /** Builder de {@link WebhookVerifier}. */
    public static final class Builder {
        private Duration tolerance = DEFAULT_TOLERANCE;
        private Clock clock = Clock.systemUTC();

        private Builder() {}

        /**
         * Janela de tolerância do timestamp. Padrão: 5 minutos.
         *
         * @param value a tolerância
         * @return este builder
         */
        public Builder tolerance(final Duration value) {
            this.tolerance = value;
            return this;
        }

        /**
         * Relógio usado na checagem da janela; útil em testes.
         *
         * @param value o relógio
         * @return este builder
         */
        public Builder clock(final Clock value) {
            this.clock = value;
            return this;
        }

        /**
         * Constrói o verificador.
         *
         * @return o verificador
         */
        public WebhookVerifier build() {
            return new WebhookVerifier(tolerance, clock);
        }
    }
}
