# Changelog

Todas as mudanças relevantes deste projeto são documentadas aqui. O formato segue o [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/) e o projeto adota o [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [Não lançado]

### Adicionado

- `CreateBroadcastInput.Builder.scheduledAt(Instant)` agenda a campanha (ela nasce `scheduled`).
- `BroadcastDetail.metrics()` (`Optional<BroadcastMetrics>`) com entregues, abertos, cliques, bounces, falhas e suprimidos.
- `Senders.resendVerification(String)` e `SendersAsync.resendVerification(String)` reenviam o e-mail de verificação com token novo.
- `ContactDetail.subscribed()` e `ContactDetail.isSubscribed()` (`false` quando o contato se descadastrou).
- Os construtores anteriores de `BroadcastDetail` e `ContactDetail` continuam disponíveis (compatibilidade binária verificada pelo `verifyApiCompatibility`).

### Alterado

- `SenderDetail.verificationToken()` vem sempre vazio em `senders().create()`: a API não devolve mais o token, que segue por e-mail ao endereço cadastrado.
- `broadcasts().send()` responde assim que o envio é enfileirado (`SENDING`); o progresso acompanha-se por `broadcasts().get()`.

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
