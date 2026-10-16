# Changelog

Todas as mudanças relevantes deste projeto são documentadas aqui. O formato segue o [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/) e o projeto adota o [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [Não lançado]

### Adicionado

- `CreateBroadcastInput.Builder.scheduledAt(Instant)` agenda a campanha (ela nasce `scheduled`).
- `BroadcastDetail.metrics()` (`Optional<BroadcastMetrics>`) com entregues, abertos, cliques, bounces, falhas e suprimidos.
- `Senders.resendVerification(String)` e `SendersAsync.resendVerification(String)` reenviam o e-mail de verificação com token novo.
- `ContactDetail.subscribed()` e `ContactDetail.isSubscribed()` (`false` quando o contato se descadastrou).
- Os construtores anteriores de `BroadcastDetail` e `ContactDetail` continuam disponíveis (compatibilidade binária verificada pelo `verifyApiCompatibility`).
- `Domains.rotateDkim(String)` (e `DomainsAsync`) inicia a rotação da chave DKIM e devolve `DkimRotation` (`domainId`, `selector`, `startedAt`, `record`). Idempotente; erros `DOMAIN_NOT_FOUND` (404) e `DOMAIN_NOT_VERIFIED` (409) em `apiCode()`.
- `Domains.setTrackingDomain(String, String)` e `Domains.removeTrackingDomain(String)` (e `DomainsAsync`) cadastram e removem o host de tracking próprio, com `CustomHostStatus` (`host`, `target`, `verified`, `verifiedAt`, `record`). Erros `INVALID_TRACKING_HOST` (400), `TRACKING_DOMAINS_DISABLED` (409) e 404 em `apiCode()`.
- `DomainDetail.dkimRotation()`, `returnPath()`, `trackingDomain()` e `trackingDomainsAvailable()`.
- `DomainWarmupStatus.status()` (`DomainWarmupState`: `WARMING`, `COMPLETED`), `totalDays()`, `startedAt()` e `completedAt()`.
- Os construtores anteriores de `DomainDetail` e `DomainWarmupStatus` continuam disponíveis (compatibilidade binária verificada pelo `verifyApiCompatibility`).
- `Templates.listVersions(String)` e `listVersions(String, OffsetPaginationQuery)` (e `TemplatesAsync`) listam as versões gravadas de um template em `ListTemplateVersionsResponse` (`data` com `TemplateVersionSummary`, `total`).
- `Templates.getVersion(String, int)` devolve `TemplateVersionDetail` com o conteúdo completo da versão; `Templates.restoreVersion(String, int)` restaura a versão e devolve o `TemplateDetail`. Erros `TEMPLATE_NOT_FOUND` e `TEMPLATE_VERSION_NOT_FOUND` (404) em `apiCode()`.
- `Webhooks.getDelivery(String, String)` e `Webhooks.replayDelivery(String, String)` (e `WebhooksAsync`) com `WebhookDeliveryDetail` (campos da entrega mais `requestPayload`; `delivery()` converte para `WebhookDelivery`). Erros `WEBHOOK_NOT_FOUND`/`WEBHOOK_DELIVERY_NOT_FOUND` (404) e `WEBHOOK_DELIVERY_PENDING`/`WEBHOOK_NOT_ACTIVE` (409) em `apiCode()`.
- Recurso `CoffeeMail.validation()` / `CoffeeMailAsync.validation()` com `validate(List<String>)`: valida até 100 endereços e devolve `ValidateEmailsResponse` (`EmailValidationResult` por endereço, totais e `ValidationCredits`). Créditos do mês esgotados viram `RateLimitException` com `apiCode()` `VALIDATION_CREDITS_EXHAUSTED`, que não deve ser retentado.

### Alterado

- `SenderDetail.verificationToken()` vem sempre vazio em `senders().create()`: a API não devolve mais o token, que segue por e-mail ao endereço cadastrado.
- `broadcasts().send()` responde assim que o envio é enfileirado (`SENDING`); o progresso acompanha-se por `broadcasts().get()`.
- `SendEmailInput.Builder.templateId(String)` aceita o id ou o alias do template.

### Removido

- `CoffeeMailBuilder.baseUrl(String)` e `baseUrl(URI)`. O SDK fala só com `https://api.coffeemail.com.br`, para que a chave de API nunca seja enviada a outro host. É uma quebra de compatibilidade intencional, excluída do `verifyApiCompatibility`.

## [0.1.0] - 2026-10-14

### Adicionado

- Clientes `CoffeeMail` (síncrono) e `CoffeeMailAsync` (`CompletableFuture`) com o mesmo contrato dos SDKs Node.js, Python e PHP.
- Recursos `emails`, `domains`, `templates`, `audiences` (com `contacts`), `broadcasts`, `senders`, `suppressions`, `webhooks` e `stats`, cobrindo 61 operações da API de produto.
- Envelope `CoffeeMailResponse<T>` com `data()`, `error()`, `unwrap()`, `fold()` e `map()`.
- Hierarquia de erros com código canônico (`code()`), código original da API (`apiCode()`), metadados (`details()`) e `requestId()`.
- `WebhookVerifier`, compatível com a assinatura enviada pela API (`sha256=`, timestamp e chave de idempotência, janela de 5 minutos).
- Introspecção da chave de API com cache de 5 minutos.
- Mensagens de erro em português (padrão) e inglês.
- Módulo JPMS `br.com.coffeemail`.
