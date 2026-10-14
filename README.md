# CoffeeMail Java SDK

[![CI](https://github.com/Coffee-Mail/coffee-mail-sdk-java/actions/workflows/ci.yml/badge.svg)](https://github.com/Coffee-Mail/coffee-mail-sdk-java/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/br.com.coffeemail/coffeemail-java)](https://central.sonatype.com/artifact/br.com.coffeemail/coffeemail-java)
[![Licença: MIT](https://img.shields.io/badge/licen%C3%A7a-MIT-blue.svg)](LICENSE)

SDK oficial da [CoffeeMail](https://coffeemail.com.br) para Java 17+: envio transacional, domínios, templates, audiências, campanhas, supressões, webhooks e estatísticas.

## ✨ Visão Geral

- **Mesmo contrato dos SDKs oficiais** (Node.js, Python e PHP): mesmos recursos, mesmos métodos e o retorno seguro `{ data, error }`.
- **Síncrono e assíncrono**: `CoffeeMail` e `CoffeeMailAsync` (com `CompletableFuture`) expõem exatamente as mesmas operações.
- **Nenhuma chamada lança exceção**: o resultado vem em `CoffeeMailResponse<T>`; use `unwrap()` quando preferir exceções.
- **Tipado de ponta a ponta**: records imutáveis, enums com valor `UNKNOWN` para compatibilidade futura e builders que exigem os campos obrigatórios em tempo de compilação.
- **Validado contra o contrato da API**: cada requisição gerada pelo SDK é testada contra o OpenAPI da CoffeeMail.
- **Leve**: usa o `java.net.http.HttpClient` do JDK e o Jackson; o transporte HTTP é plugável.
- **Pronto para JPMS**: módulo `br.com.coffeemail`.

## 📦 Instalação

Gradle (Kotlin DSL):

```kotlin
dependencies {
    implementation("br.com.coffeemail:coffeemail-java:0.1.0")
}
```

Gradle (Groovy):

```groovy
dependencies {
    implementation 'br.com.coffeemail:coffeemail-java:0.1.0'
}
```

Maven:

```xml
<dependency>
    <groupId>br.com.coffeemail</groupId>
    <artifactId>coffeemail-java</artifactId>
    <version>0.1.0</version>
</dependency>
```

## 🚀 Início Rápido

```java
import br.com.coffeemail.CoffeeMail;
import br.com.coffeemail.CoffeeMailResponse;
import br.com.coffeemail.models.emails.SendEmailInput;
import br.com.coffeemail.models.emails.SendEmailResponse;

CoffeeMail coffeemail = CoffeeMail.builder()
        .apiKey("cm_live_sua_chave")
        .build();

CoffeeMailResponse<SendEmailResponse> response = coffeemail.emails().send(
        SendEmailInput.builder("Loja <contato@seudominio.com.br>", "cliente@gmail.com")
                .subject("Pedido #123 confirmado")
                .html("<h1>Obrigado pela compra!</h1>")
                .idempotencyKey("pedido-123")
                .build());
```

Sem `apiKey(...)`, a chave é lida da variável de ambiente `COFFEEMAIL_API_KEY` (`CoffeeMail.create()` faz isso com as configurações padrão). O cliente é thread-safe: crie uma instância e reutilize-a, por exemplo como um bean singleton no Spring.

| Opção do builder | Padrão |
| --- | --- |
| `apiKey(String)` | `COFFEEMAIL_API_KEY` |
| `baseUrl(String)` | `https://api.coffeemail.com.br` |
| `locale(CoffeeMailLocale)` | `PT_BR` (mensagens de erro e `Accept-Language`) |
| `timeout(Duration)` | 10 segundos |
| `transport(HttpTransport)` | `java.net.http.HttpClient` |

## 🛡️ Retorno Seguro `{ data, error }`

```java
CoffeeMailResponse<SendEmailResponse> response = coffeemail.emails().send(input);

if (response.isSuccess()) {
    System.out.println(response.data().orElseThrow().id());
}

response.error().ifPresent(error -> {
    System.err.println(error.code() + " " + error.getMessage());
    error.requestId().ifPresent(id -> System.err.println("x-request-id: " + id));
});

String id = coffeemail.emails().send(input).unwrap().id();

String message = response.fold(
        sent -> "Enfileirado: " + sent.id(),
        error -> "Falhou: " + error.getMessage());
```

### Erros

Todas as falhas estendem `CoffeeMailException` (unchecked) e expõem `status()`, `code()` (código canônico, igual entre os SDKs), `apiCode()` (código original da API, como `EMAIL_NOT_FOUND`), `details()` e `requestId()`.

| Java | Node.js / Python | HTTP |
| --- | --- | --- |
| `ValidationException` | `ValidationError` | 400 |
| `AuthenticationException` | `AuthenticationError` | 401 |
| `PaymentRequiredException` | `PaymentRequiredError` | 402 |
| `ForbiddenException` | `ForbiddenError` | 403 |
| `NotFoundException` | `NotFoundError` | 404 |
| `ConflictException` | `ConflictError` | 409 |
| `RateLimitException` (`retryAfterSeconds()`) | `RateLimitError` | 429 |
| `InternalServerException` | `InternalServerError` | 500, 502, 503, 504 |
| `NetworkException` | `NetworkError` | sem resposta (rede ou timeout) |
| `PermissionException` (`requiredPermission()`) | `PermissionError` | 403 em campanhas |

## ⚡ Cliente Assíncrono

```java
CoffeeMailAsync coffeemail = CoffeeMailAsync.builder().apiKey("cm_live_sua_chave").build();

coffeemail.emails().send(input)
        .thenAccept(response -> response.data().ifPresent(sent -> log.info("Enviado: {}", sent.id())));
```

Os futuros nunca completam com exceção por erro de API: o erro vem no envelope, como no cliente síncrono.

## 📚 Recursos Disponíveis

Os recursos são interfaces (`br.com.coffeemail.resources`), fáceis de simular nos seus testes.

### 1. E-mails (`coffeemail.emails()`)

`send`, `sendBatch`, `get`, `list`, `getEvents`, `getTags`, `cancel`, `resend`.

```java
EmailAttachment nota = EmailAttachment.fromBytes("nota.pdf", bytes).withContentType("application/pdf");

coffeemail.emails().send(SendEmailInput.builder("contato@loja.com.br", "cliente@gmail.com")
        .cc("financeiro@cliente.com.br")
        .templateId("7c8d3f2b-8c1e-4a7d-be5f-3e4f5a601c2d")
        .variables(Map.of("nome", "Ana"))
        .attachments(nota)
        .tags(new EmailTag("pedido", "123"))
        .scheduledAt(Instant.parse("2026-12-01T09:00:00Z"))
        .sandbox(true)
        .build());

coffeemail.emails().list(ListEmailsQuery.builder().status(EmailStatus.BOUNCED).limit(50).build());
```

### 2. Domínios (`coffeemail.domains()`)

`create`, `list`, `get`, `verify`, `delete`, `getHealth`, `getWarmupStatus`.

### 3. Templates (`coffeemail.templates()`)

`create`, `list`, `get`, `update`, `delete`, `preview`, `previewById`, `format`, `testRender`, `testSend`.

```java
coffeemail.templates().update(templateId, UpdateTemplateInput.builder()
        .subject("Novo assunto")
        .clearPreheader()
        .build());
```

Nas atualizações parciais, só os campos definidos no builder são enviados; os métodos `clear*` limpam o valor.

### 4. Audiências e Contatos (`coffeemail.audiences()`)

`create`, `list`, `get`, `update`, `delete`, `contacts()` (`create`, `list`, `bulkAdd`, `update`, `delete`) e os atalhos `listContacts`, `createContact`, `bulkAddContacts`.

### 5. Campanhas (`coffeemail.broadcasts()`)

`create`, `list`, `get`, `send`, `cancel`, `testSend`. Exige uma chave com permissão `full_access`; caso contrário, o erro é `PermissionException`.

```java
coffeemail.broadcasts().create(
        CreateBroadcastInput.fromTemplate(audienceId, "news@loja.com.br", templateId).build());
```

### 6. Remetentes (`coffeemail.senders()`)

`create`, `list`, `verify`, `delete`.

### 7. Supressões (`coffeemail.suppressions()`)

`list`, `get`, `create`, `delete`, `reactivate`, `bulkCreate`.

### 8. Webhooks (`coffeemail.webhooks()`)

`create`, `list`, `get`, `update`, `toggle`, `delete`, `rotateSecret`, `listDeliveries`, `test`, `verifySignature`.

### 9. Estatísticas (`coffeemail.stats()`)

```java
coffeemail.stats().get(GetStatsQuery.builder().period(StatsPeriod.LAST_30D).granularity(StatsGranularity.WEEK).build());
```

### 10. Introspecção da Chave de API

`coffeemail.introspect()` retorna os metadados da chave em uso, com cache de 5 minutos (`introspect(true)` ignora o cache; `invalidateApiKeyCache()` o descarta).

## 🔒 Validação Criptográfica de Webhooks

Cada entrega traz os cabeçalhos `x-webhook-signature`, `x-webhook-timestamp` e `x-webhook-idempotency-key`. A assinatura é `sha256=` + HMAC-SHA256 de `timestamp.idempotencyKey.corpo`; entregas com mais de 5 minutos são rejeitadas para impedir replay. Valide sempre o **corpo bruto**, nunca o JSON reserializado.

```java
@PostMapping("/webhooks/coffeemail")
public ResponseEntity<Void> receive(@RequestBody String rawBody, @RequestHeader Map<String, String> headers) {
    boolean authentic = WebhookVerifier.verifySignature(
            VerifyWebhookInput.fromHeaders(rawBody, headers, System.getenv("COFFEEMAIL_WEBHOOK_SECRET")));
    if (!authentic) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    return ResponseEntity.noContent().build();
}
```

Use o `x-webhook-idempotency-key` para descartar entregas duplicadas.

## 🛠️ Desenvolvimento

Requer JDK 17+ (o Gradle provisiona o JDK 21 usado na compilação).

```bash
./gradlew check            # formatação, Checkstyle, Error Prone + NullAway, testes, cobertura e contrato
./gradlew spotlessApply    # formata o código
./gradlew syncOpenApi      # atualiza spec/product.json a partir do coffee-mail-api
./gradlew test -PtestJavaVersion=21
```

Veja o [CONTRIBUTING.md](CONTRIBUTING.md) antes de abrir um PR.

## 📄 Licença

[MIT](LICENSE).
