package br.com.coffeemail.models.audiences;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Atualização parcial de uma audiência. Apenas os campos definidos no builder são enviados; os métodos {@code clear*} enviam
 * {@code null} para limpar o valor.
 *
 * @param fields campos que serão enviados à API
 */
public record UpdateAudienceInput(@JsonValue Map<String, Object> fields) {

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String ACTIVE = "active";

    /**
     * Copia os campos para um mapa imutável que preserva a ordem e valores nulos.
     *
     * @param fields campos que serão enviados à API
     */
    public UpdateAudienceInput {
        fields = Collections.unmodifiableMap(new LinkedHashMap<>(fields));
    }

    /**
     * Inicia a construção.
     *
     * @return o builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder de {@link UpdateAudienceInput}. */
    public static final class Builder {
        private final Map<String, Object> fields = new LinkedHashMap<>();

        private Builder() {}

        public Builder name(final String value) {
            fields.put(NAME, value);
            return this;
        }

        public Builder description(final String value) {
            fields.put(DESCRIPTION, value);
            return this;
        }

        public Builder clearDescription() {
            fields.put(DESCRIPTION, null);
            return this;
        }

        public Builder active(final Boolean value) {
            fields.put(ACTIVE, value);
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public UpdateAudienceInput build() {
            return new UpdateAudienceInput(fields);
        }
    }
}
