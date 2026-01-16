# Cadastro — Aplicação de geração e registro de codinomes

Este repositório contém uma aplicação Spring Boot simples que gera e registra "codinomes" para jogadores a partir de catálogos externos (por exemplo: Vingadores, Liga da Justiça). O projeto utiliza um banco H2 em memória para armazenar os registros e uma camada de serviço que seleciona codinomes não utilizados por grupo.

Resumo rápido
- Linguagem: Java 17
- Framework: Spring Boot
- Banco: H2 (in-memory)
- Arquitetura: camadas (web → service → repository → persistence)

Arquitetura e organização do código

- model/
  - `Jogador` (record): representa o jogador com os campos nome, email, telefone, codinome e grupoCodinome.
  - `GrupoCodinome` (enum): enumeração dos grupos de codinomes disponíveis (VINGADORES, LIGA_DA_JUSTICA) e a URI de onde buscar a lista.

- service/
  - `CodinomeService`: lógica para gerar um codinome. Filtra codinomes já em uso e sorteia um dos disponíveis; lança exceção se não houver mais codinomes livres.
  - `CodinomesRepositoryFactory`: fábrica que retorna a implementação correta de `CodinomeRepository` para cada `GrupoCodinome`.

- repository/
  - `CodinomeRepository` (interface): contrato para buscar codinomes (List<String> buscarCodinomes()).
  - `VingadoresRepository` / `LigaDaJusticaRepository`: implementações que consultam as URIs definidas em `GrupoCodinome` via cliente REST e fazem parsing do conteúdo para extrair os codinomes.
  - `JogadorRepository`: persiste um `Jogador` no banco usando `JdbcClient`.

- resources/
  - `schema.sql`: cria a tabela de jogadores e define uma restrição de unicidade em (codinome, grupoCodinome).
  - `application.properties`: configura H2 em memória, inicialização dos scripts SQL e H2 console.

Regras de negócio (implementadas)

1. Cada jogador tem um `codinome` e um `grupoCodinome` (por exemplo, "Vingadores" ou "Liga da Justiça").
2. Um mesmo codinome não pode ser registrado duas vezes dentro do mesmo grupo — existe uma restrição de unicidade (UC_CODINOME_GRUPO) na base de dados para (codinome, grupoCodinome).
3. Ao gerar um codinome para um jogador o sistema:
   - Obtém a lista completa de codinomes do catálogo correspondente ao `GrupoCodinome`.
   - Filtra os codinomes já em uso (lista fornecida ao método `CodinomeService.gerarCodinome`).
   - Se houver codinomes disponíveis, escolhe aleatoriamente um deles e o retorna.
   - Se não houver codinomes disponíveis, lança uma RuntimeException indicando indisponibilidade.
4. A seleção é feita de forma aleatória simples (Math.random). Não há garantia de uniformidade além da distribuição básica de Java.

Integração com fontes externas

- As URIs das fontes externas são definidas no enum `GrupoCodinome` (campo `uri`). As implementações de repositório constroem um cliente REST e fazem uma requisição para buscar o catálogo.
- O projeto contém DTOs para desserialização: `VingadoresDTO` (contém lista de objetos contendo o campo `codinome`) e uma interface `CodinomeDTO` com método `getCodinomes()`.

Banco de dados

O arquivo `src/main/resources/schema.sql` contém a criação da tabela:

- Tabela: jogadores (ou JOGADOR — ver Observações abaixo)
- Colunas: id, nome, email, telefone, codinome, grupoCodinome
- Constraint: UNIQUE(codinome, grupoCodinome) para evitar duplicidade de codinome por grupo

Como executar (Windows PowerShell)

1. Construir e executar a aplicação:

```powershell
# Na raiz do projeto
.\mvnw.cmd spring-boot:run
```

2. A aplicação inicia em http://localhost:8080 por padrão (configuração Spring Boot). O console H2 fica disponível em http://localhost:8080/h2-console (conforme `application.properties`).

Observações, riscos e melhorias sugeridas

- Observação de consistência de nomes: o `schema.sql` cria a tabela `jogadores` (plural, minúsculo), enquanto `JogadorRepository` insere em `JOGADOR` (singular, maiúsculo). Isso pode causar erro de SQL em tempo de execução dependendo do dialeto e do comportamento do banco (H2 costuma ser case-insensitive por padrão, mas vale conferir). Recomenda-se alinhar o nome da tabela e as colunas entre o script SQL e as queries do repositório.

- As classes `VingadoresRepository` e `LigaDaJusticaRepository` usam um cliente REST e um ObjectMapper para desserializar a resposta. No código atual há imports/uso que parecem incorretos (por exemplo `tools.jackson.databind.ObjectMapper` em vez de `com.fasterxml.jackson.databind.ObjectMapper`). Recomendo revisar os imports e testar a desserialização com as URIs reais.

- Headers HTTP e media types: os repositórios configuram headers (`Content-Type: application/json` e `Accept: text/plain`), o que parece inconsistente; reveja se a API externa retorna JSON ou XML (o enum `GrupoCodinome` sugere que Liga da Justiça usa XML) e ajuste `Accept`/parsers conforme necessário.

- Não encontrei controllers HTTP no projeto (não há classes anotadas com `@RestController`/`@Controller`). Se a intenção for expor endpoints REST ou uma interface web (Thymeleaf está presente nas dependências), será necessário implementar controllers que usem `CodinomeService` e `JogadorRepository`.

- Testes e validações: adicionaria testes unitários para o `CodinomeService` (caminho feliz e caso onde não há codinomes disponíveis) e testes de integração para a persistência com H2.

Checklist de entrega
- [x] README com descrição da aplicação
- [x] Arquitetura e mapeamento de classes principais
- [x] Regras de negócio documentadas
- [x] Observações de possíveis problemas e sugestões de melhoria

