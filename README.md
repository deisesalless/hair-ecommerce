# 🧴 E-commerce de Produtos de Cabelo

> **Status:** Em desenvolvimento | **Início:** 27/04/2026 | **Última atualização:** 01/05/2026
>
> Projeto de portfólio para consolidação de skills em Java, Spring Boot,
> CI/CD e cloud. Evolução documentada por milestones.

🔗 **Repositório:** [https://github.com/deisesalless/hair-ecommerce](link)

🚀 **Deploy:** Em breve — primeiro deploy após CRUD completo de brand + endpoint /health

---

## ✅ O QUE JÁ FUNCIONA (Milestone Atual: Fundação)

| Feature | Status | Detalhes |
|---------|--------|----------|
| Configuração Spring Boot 3.5 | ✅ | Gerado via Spring Initializr, com inclusão manual de dependências (Flyway, MapStruct, PostgreSQL) |
| PostgreSQL + Docker Compose | ✅ | Banco rodando local |
| Flyway + seed de dados | ✅ | 5 tabelas, dados de exemplo |
| SonarLint local | ✅ | Plugin IDE configurado |

---

## 🎯 PRÓXIMO MILESTONE (em andamento)

### Milestone 1: CRUD Produto + Qualidade (até 15/05)
- [ ] CRUD completo (entity → repository → service → controller)
    - [x] brand
      - [ ] criar (em andamento)
      - [x] listar + busca por id
      - [ ] atualizar + ativar + desativar
    - [ ] category
        - [ ] criar (em andamento)
        - [ ] listar + busca por id
        - [ ] atualizar + ativar + desativar
    - [ ] product
        - [ ] criar (em andamento)
        - [ ] listar + busca por id
        - [ ] atualizar + ativar + desativar
    - [ ] product_variation
        - [ ] criar (em andamento)
        - [ ] listar + busca por id
        - [ ] atualizar + ativar + desativar
    - [ ] stock
        - [ ] criar (em andamento)
        - [ ] listar + busca por id
        - [ ] atualizar + ativar + desativar
- [x] DTOs (Record) + MapStruct
- [ ] Exceções customizadas + @ControllerAdvice
- [ ] Logs estruturados (JSON)
- [ ] Profiles (dev/test/prod)
- [ ] **Deploy inicial** (Render/Railway — mesmo que só health check)

> **Decisão tomada:** Testes de integração com Testcontainers para toda entidade.
> Testes unitários para toda regra de negócio. H2 apenas para testes de unidade
> que não tocam banco.

---

## 📋 BACKLOG (não datado — priorizado)

| Prioridade | Milestone | Stack |
|------------|-----------|-------|
| P1 | CRUD Variação + Estoque | Spring Data JPA, Flyway |
| P2 | Paginação + Filtros SQL | Specifications ou QueryDSL |
| P3 | Testes + Swagger | JUnit, Mockito, Testcontainers, SpringDoc |
| P4 | CI/CD + Deploy | GitHub Actions, Docker multistage |
| P5 | Mensageria (RabbitMQ) | Spring AMQP, eventos de estoque |
| P6 | Pedidos + Alertas reais | JWT, Spring Security, email |
| P7 | Métricas + Observabilidade | Micrometer, Prometheus |
| P8 | Refatoração Clean Architecture/DDD | *Apenas quando o básico estiver sólido* |

> **Nota:** Milestones P5+ só começam quando P1-P4 estiverem no ar e estáveis.

---

## 🏗️ ARQUITETURA ATUAL
```
Fase 1: Arquitetura em Camadas
├── controller
├── service
├── repository
├── entity
├── dto
├── exception
└── config
```


**Transição para Clean Architecture/DDD:** será avaliada quando:
- [ ] Domínio tiver 4+ entidades com regras de negócio não-triviais
- [ ] Pelo menos 1 regra de negócio cruzar 2+ entidades
- [ ] Testes de integração cobrirem 80%+ dos endpoints

---

## 🛠️ STACK TÉCNICA

### Implementado
- Java 21, Spring Boot 3.5, PostgreSQL, Flyway, Docker Compose, MapStruct, Lombok, Spring Validation, DTOs

### Em implementação (Milestone 1)
- Spring Web, Spring Data JPA, Logs estruturados, Exceções customizadas

### Planejado (Backlog)
- JUnit 5, Mockito, Jacoco, Testcontainers, SpringDoc, RabbitMQ, Spring Security, JWT,
  GitHub Actions(CI/CD), Render/Railway, Micrometer

---

## 🧪 ESTRATÉGIA DE TESTES

| Tipo | O quê | Ferramenta | Banco |
|------|-------|------------|-------|
| Unitário | Services, mappers, utils | JUnit + Mockito | Nenhum (mocks) |
| Integração | Endpoints, queries, migrations | MockMvc/RestAssured | **Testcontainers PostgreSQL** |

> **Por que não H2 para integração:** Flyway pode quebrar, tipos JSON/JSONB não
> existem, locks comportam-se diferente. [Issue #1: Por que não usar H2 para testes de integração](https://github.com/deisesalless/hair-ecommerce/issues/1)

---

## 🚀 DEPLOYMENT

- [ ] Dockerfile multistage
- [ ] GitHub Actions (build → test → deploy)
- [ ] Variáveis de ambiente externas (nunca secrets no repo)

**Deploy atual:** Nenhum — primeiro deploy no Milestone 1.

---

## 📊 DECISÕES ARQUITETURAIS DOCUMENTADAS

| Data  | Decisão                                 | Contexto                                                                        | Alternativa rejeitada |
|-------|-----------------------------------------|---------------------------------------------------------------------------------|----------------------|
| 27/04 | Spring Initializr como ponto de partida | Produtividade inicial, foco em lógica de negócio | Configuração 100% manual do `pom.xml` |
| 27/04 | PostgreSQL + Docker Compose             | Ambiente próximo de produção                                                    | H2 para desenvolvimento |
| 28/04 | Seed Data + Flyway migrations           | Analisar estrutura do banco de dados com o teste de listagem e encontrar por id | Adicionar depois  |
| 28/04 | SonarLint desde o início                | Qualidade como hábito, não afterthought                                         | Adicionar depois |
| 30/04 | Lombok + MapStruct habilitados          | Reduzir boilerplate e separação da entidade e DTO                               | Records (limitado para JPA) |
| 30/04 | Record DTOs em vez de classes           | Imutabilidade, menos boilerplate, nativo do Java | Classes tradicionais com Lombok @Data |

---

## 🎯 APRENDIZADOS E DIFICULDADES

**30/04/2026 — Migrations e auditoria de dados**
> Utilização das migrations poderia ter sido mais otimizada. Nas tabelas iniciais
> não incluí `created_at`/`updated_at`, o que dificultará filtros temporais e
> relatórios no futuro. **Ação:** revisar schema antes do primeiro deploy
> (Milestone 1).
