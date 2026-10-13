package br.com.coffeemail;

/** Idioma das mensagens de erro do SDK e do cabeçalho {@code Accept-Language} enviado à API. */
public enum CoffeeMailLocale {
    /** Português do Brasil (padrão). */
    PT_BR("pt-BR"),
    /** Inglês. */
    EN("en");

    private final String tag;

    CoffeeMailLocale(final String tag) {
        this.tag = tag;
    }

    /**
     * Tag BCP 47 enviada no cabeçalho {@code Accept-Language}.
     *
     * @return a tag do idioma, como {@code pt-BR}
     */
    public String tag() {
        return tag;
    }
}
