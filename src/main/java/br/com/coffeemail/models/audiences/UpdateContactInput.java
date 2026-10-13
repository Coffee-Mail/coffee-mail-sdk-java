package br.com.coffeemail.models.audiences;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Atualização parcial de um contato. Apenas os campos definidos no builder são enviados; os métodos {@code clear*} enviam
 * {@code null} para limpar o valor.
 *
 * @param fields campos que serão enviados à API
 */
public record UpdateContactInput(@JsonValue Map<String, Object> fields) {

    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String METADATA = "metadata";

    /**
     * Copia os campos para um mapa imutável que preserva a ordem e valores nulos.
     *
     * @param fields campos que serão enviados à API
     */
    public UpdateContactInput {
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

    /** Builder de {@link UpdateContactInput}. */
    public static final class Builder {
        private final Map<String, Object> fields = new LinkedHashMap<>();

        private Builder() {}

        public Builder firstName(final String value) {
            fields.put(FIRST_NAME, value);
            return this;
        }

        public Builder clearFirstName() {
            fields.put(FIRST_NAME, null);
            return this;
        }

        public Builder lastName(final String value) {
            fields.put(LAST_NAME, value);
            return this;
        }

        public Builder clearLastName() {
            fields.put(LAST_NAME, null);
            return this;
        }

        public Builder metadata(final Map<String, Object> value) {
            fields.put(METADATA, value);
            return this;
        }

        /**
         * Constrói a instância.
         *
         * @return a instância imutável
         */
        public UpdateContactInput build() {
            return new UpdateContactInput(fields);
        }
    }
}
