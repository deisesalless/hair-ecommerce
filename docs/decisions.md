**30/04/2026 — Migrations e auditoria de dados**
> Utilização das migrations poderia ter sido mais otimizada. Nas tabelas iniciais
> não incluí `created_at`/`updated_at`, o que dificultará filtros temporais e
> relatórios no futuro. **Ação:** revisar schema antes do primeiro deploy
> (Milestone 1).

**01/05/2026 — @PrePersist & Decisão de Timestamp**
> Implementei `@PrePersist` na entidade `Brand` para garantir que os campos `is_active` e `created_at`
> sejam populados no momento da persistência, independentemente dos defaults do banco. Isso resolveu o
> problema de retorno de valores `null` no DTO após `repository.save()`, eliminando a necessidade de um
> `findById` adicional.
>
> Os defaults no DDL (`DEFAULT true`, `DEFAULT CURRENT_TIMESTAMP`) foram mantidos como **camada de
> segurança** para inserts manuais diretos no banco, mas a **fonte primária de verdade** permanece no
> código da aplicação.
>
> Essa abordagem também facilita testes unitários com `Clock` fixo, permitindo datas determinísticas
> sem depender do ambiente de execução.
>
> **Pendência técnica:** avaliar migração de `LocalDateTime` para `Instant` (UTC). O `Instant` elimina
> ambiguidade de fuso horário em arquiteturas distribuídas — cenário comum quando o banco e o backend
> residem em servidores distintos — e alinha o projeto ao padrão ISO 8601 para APIs REST.
>


**01/05/2026 — Tratamento Global de Exceções com `@RestControllerAdvice`**

> Migrei de `@ControllerAdvice` para `@RestControllerAdvice` para centralizar o tratamento de exceções HTTP em 
> toda a aplicação. A anotação compõe `@ControllerAdvice` + `@ResponseBody`, eliminando a necessidade de decorar 
> métodos do handler com `@ResponseBody` explicitamente.
>
> Implementei hierarquia de exceções com `BusinessException` como classe base para regras de domínio, separando 
> **falhas técnicas** (500) de **violações de negócio** (400/404/409). As exceções customizadas 
> (`ResourceNotFoundException`) são lançadas nos Services e traduzidas para respostas padronizadas pelo handler, 
> garantindo que o cliente sempre receba JSON estruturado com `timestamp`, `status`, `message` e `path` — nunca 
> stack trace exposta.
>
> Optei por não estender `ResponseEntityExceptionHandler` e usar `@ExceptionHandler` diretamente. Isso evita 
> conflitos de nullabilidade (`@Nullable` vs `@NonNullApi`) reportados pelo SonarLint e reduz acoplamento com 
> classes internas do Spring MVC — suficiente para a complexidade atual do projeto.
>
> **Pendência técnica:** estudar e avaliar adoção de `ProblemDetail` (RFC 7807), padrão moderno do Spring Boot 
> 3.2+ para APIs REST.

**01/05/2026 — `@Transactional(readOnly = true)` em Consultas**
> Adotei `@Transactional(readOnly = true)` em todos os métodos de leitura do `BrandService`.
> Embora o Spring Data JPA já gerencie transações internamente em repositories, a anotação
> no service garante que múltiplas queries relacionadas (ex: buscar marca + estatísticas)
> executem em uma única transação, além de desabilitar flush automático e dirty checking
> do Hibernate — reduzindo overhead de memória e melhorando performance em operações puramente
> de leitura.
>
> Métodos de escrita (`create`, `update`, `disable`) permanecem com `@Transactional` padrão
> (`readOnly = false`), garantindo que modificações sejam commitadas corretamente.

**01/05/2026 — Tratamento Global de Exceções com `@RestControllerAdvice`**