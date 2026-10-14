# Changelog

Todas as mudanças relevantes deste projeto são documentadas aqui. O formato segue o [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/) e o projeto adota o [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [Não lançado]

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
