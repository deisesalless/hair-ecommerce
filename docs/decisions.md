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

**01/05/2026 — Health Check com Spring Boot Actuator**

> Adicionei endpoint `/actuator/health` via Spring Boot Actuator para monitoramento
> da aplicação. Implementei `DatabaseHealthIndicator` customizado que valida
> conexão com PosgteSQL em tempo real, retornando `UP` ou `DOWN` com detalhes.
>
> Em produção, health checks são consumidos por load balancers (AWS ALB) e
> orquestradores (Kubernetes probes) para decisões automáticas de roteamento
> e reinicialização. Configurei `show-details: always` em dev/test e
> `when_authorized` para produção, evitando vazamento de informações de infraestrutura.
>
> **Pendência:** implementar separação `livenessProbe` vs `readinessProbe`
> quando migrar para Kubernetes.

**01/05/2026 - Anotação @Sl4j**

> Optei por usar `@Slf4j` do Lombok para logs estruturados e padronizados. Essa anotação 
> evita boilerplate de declaração manual do logger (`private static final Logger log = 
> LoggerFactory.getLogger(BrandService.class);`) e garante que todas as classes tenham um 
> logger consistente, facilitando a manutenção e leitura dos logs em ambientes de produção.
> 
>  **Pendência:** analisar a implementação de um log JSON (MDC/traceId) quando o projeto evoluir para eventos.

