package br.com.coffeemail.errors;

import br.com.coffeemail.internal.constants.HttpStatus;

/** A chave de API não possui a permissão exigida pela operação, como {@code full_access}. */
public final class PermissionException extends CoffeeMailException {

    private static final long serialVersionUID = 1L;

    private final String requiredPermission;

    /**
     * Cria o erro.
     *
     * @param error detalhes do erro
     * @param requiredPermission permissão exigida
     */
    public PermissionException(final ApiErrorDetails error, final String requiredPermission) {
        super(HttpStatus.NONE, ErrorCodes.PERMISSION_DENIED, error);
        this.requiredPermission = requiredPermission;
    }

    /**
     * Permissão exigida pela operação.
     *
     * @return a permissão, como {@code full_access}
     */
    public String requiredPermission() {
        return requiredPermission;
    }
}
