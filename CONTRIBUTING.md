# Contribuindo com o CoffeeMail Java SDK

Obrigado pelo interesse! Este guia resume como propor mudanças.

## Antes de começar

- Bugs e sugestões: abra uma issue usando um dos modelos.
- Vulnerabilidades: **não** abra issue pública; siga o [SECURITY.md](SECURITY.md).
- Mudanças grandes ou que alterem a API pública: abra uma issue para discussão antes do PR.

## Ambiente

- JDK 17 ou superior. O Gradle provisiona sozinho o JDK 21 usado na compilação.
- `./gradlew check` precisa passar antes de qualquer PR. Ele roda formatação (Spotless), Checkstyle, Error Prone com NullAway, testes, cobertura mínima (85% de linhas e 78% de branches) e a conformidade das requisições com o OpenAPI da API.

## IDE

A nulidade segue o [JSpecify](https://jspecify.dev): todos os pacotes são `@NullMarked`, e o `check` verifica com o NullAway. Para IDEs baseadas no Eclipse JDT (VS Code/Antigravity com a extensão Java da Red Hat, ou o Eclipse), use as preferências versionadas em `config/eclipse/org.eclipse.jdt.core.prefs`. Elas ativam a análise de nulidade do JDT com as anotações do JSpecify e desligam apenas os avisos sobre bibliotecas sem anotações (JDK, Jackson), que o NullAway já cobre com seus modelos:

```json
"java.settings.url": "config/eclipse/org.eclipse.jdt.core.prefs"
```

Com essa configuração, o JDT não deve mostrar nenhum aviso no projeto.

## Regras de código

O SDK segue as regras de código da CoffeeMail, verificadas automaticamente pelo `check`:

- Código em inglês; documentação, issues, commits e PRs em português do Brasil.
- Sem comentários `//` ou `/* */`; Javadoc apenas na API pública.
- Sem `else`, `else if` e `switch`: use retorno antecipado ou tabelas (`Map`/`EnumMap`).
- Variáveis locais, parâmetros e campos `final` (exceções: contadores de laço e classes `*Builder`).
- Sem `try/catch` e sem `throw` fora dos pontos isolados (`TryCatch`, `CoffeeMailResponse.unwrap()`).
- Sem números e textos mágicos: use constantes nomeadas.
- Sem `System.out`/`System.err`.
- Cada comportamento novo vem com testes: caminho feliz e ao menos dois caminhos de erro.

## Compatibilidade

O SDK é usado em produção. Mudanças na API pública devem **adicionar e depreciar**, nunca renomear ou remover. A partir da 0.1.0, o `check` compara a API com a última versão publicada.

## Mudanças no contrato da API

Quando o OpenAPI da API mudar, rode `./gradlew syncOpenApi`, ajuste modelos e recursos e mantenha o `ContractConformanceTest` verde.

## Commits e PRs

- Mensagens em português, diretas, no formato `tipo(escopo): descrição` (ex.: `feat(emails): expõe metadata no envio`).
- Atualize o `CHANGELOG.md` na seção "Não lançado".
- Preencha o modelo de PR.
