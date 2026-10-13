package br.com.coffeemail.models.emails;

import br.com.coffeemail.models.support.Copies;
import java.util.Base64;
import java.util.Optional;

/**
 * Anexo de e-mail com conteúdo em Base64.
 *
 * @param filename nome exibido no cliente de e-mail, como {@code fatura.pdf}
 * @param content conteúdo em Base64
 * @param contentType MIME type, como {@code application/pdf}
 * @param disposition download convencional ou embutido no corpo
 * @param cid Content-ID para imagens embutidas ({@code cid:...})
 */
public record EmailAttachment(
        String filename, String content, String contentType, AttachmentDisposition disposition, Optional<String> cid) {

    /** MIME type usado quando nenhum é informado. */
    public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    /**
     * Cria um anexo a partir de bytes, codificando-os em Base64.
     *
     * @param filename nome do arquivo
     * @param content conteúdo bruto
     * @return o anexo
     */
    public static EmailAttachment fromBytes(final String filename, final byte[] content) {
        return fromBase64(filename, Base64.getEncoder().encodeToString(content));
    }

    /**
     * Cria um anexo a partir de conteúdo já codificado em Base64.
     *
     * @param filename nome do arquivo
     * @param base64Content conteúdo em Base64
     * @return o anexo
     */
    public static EmailAttachment fromBase64(final String filename, final String base64Content) {
        return new EmailAttachment(
                filename, base64Content, DEFAULT_CONTENT_TYPE, AttachmentDisposition.ATTACHMENT, Optional.empty());
    }

    /**
     * Copia o anexo com outro MIME type.
     *
     * @param value o MIME type
     * @return o novo anexo
     */
    public EmailAttachment withContentType(final String value) {
        return new EmailAttachment(filename, content, value, disposition, cid);
    }

    /**
     * Copia o anexo com outro modo de anexação.
     *
     * @param value o modo de anexação
     * @return o novo anexo
     */
    public EmailAttachment withDisposition(final AttachmentDisposition value) {
        return new EmailAttachment(filename, content, contentType, value, cid);
    }

    /**
     * Copia o anexo como imagem embutida, referenciável no HTML por {@code cid:valor}.
     *
     * @param value o Content-ID
     * @return o novo anexo
     */
    public EmailAttachment withCid(final String value) {
        return new EmailAttachment(filename, content, contentType, AttachmentDisposition.INLINE, Optional.of(value));
    }

    /**
     * Normaliza o Content-ID ausente.
     *
     * @param filename nome do arquivo
     * @param content conteúdo em Base64
     * @param contentType MIME type
     * @param disposition modo de anexação
     * @param cid Content-ID
     */
    public EmailAttachment {
        cid = Copies.optional(cid);
    }
}
