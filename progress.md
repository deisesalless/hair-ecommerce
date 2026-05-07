# 🧴 E-commerce de Produtos de Cabelo — Roadmap de Aprendizado

> **Objetivo:** Projeto de portfólio focado em evolução gradual. Começa com CRUD simples e caminha para arquitetura robusta. Não é pra ser perfeito de cara — é pra ser **evolutivo e bem documentado**.

---

## 📋 CHECKLIST GERAL

### Semana 1-2: Fundação (CRUD das tabelas)
- [x] Spring Boot 3.5 + config manual
- [x] PostgreSQL + Docker Compose
- [x] Flyway: criação de todas as tabelas
- [x] Seed de dados (marcas, categorias, produtos, variações, estoque)
- [x] CRUD completo (entity → repository → service → controller)
  - [x] brand
  - [ ] category
  - [ ] product
  - [ ] product_variation
  - [ ] stock
- [x] DTOs + MapStruct
- [ ] Logs estruturados
- [ ] Exceções customizadas + @ControllerAdvice global
- [ ] Profiles (dev/test/prod) — necessário pra separar H2/PostgreSQL

### Semana 3: Evoluir paginação e filtros SQL
- [ ] Paginação e ordenação com Spring Data JPA
- [ ] Filtros avançados (ex: produtos por marca, variações por categoria)
- [ ] Filtros avançados (ex: estoque por produto, variação, ou alerta de estoque baixo)

### Semana 4: Testes + Qualidade
- [ ] Testes unitários (JUnit 5 + Mockito)
- [ ] Testes de integração com **Testcontainers** (PostgreSQL real, não H2)
- [x] SonarQube local-plugin na IDE (code smells, bugs, cobertura)
- [ ] Swagger/OpenAPI (SpringDoc)

### Semana 5: CI/CD + Deploy
- [ ] GitHub Actions (build, test, SonarQube scan)
- [ ] Dockerização da aplicação (multistage build)
- [ ] Deploy no Render/Railway/Fly.io

### Semana 6-7: Mensageria
- [ ] RabbitMQ no Docker Compose
- [ ] Endpoint de "baixa de estoque" → publica evento
- [ ] Consumer processa e atualiza estoque
- [ ] Regra: estoque < 10 → publica "ESTOQUE_BAIXO"
- [ ] Consumer de alerta (simula email via log estruturado)

### Semana 8+: Evolução Arquitetural
- [ ] CRUD de Pedidos (justifica mensageria de verdade)
- [ ] Alerta de estoque baixo real (envia email - analisar hospedagem cloud com SMTP gratuito)
- [ ] JWT + Spring Security (stateless, roles: CLIENTE/ADMIN)
- [ ] Query avançada (JPQL nativo, Specifications, ou QueryDSL)
- [ ] Métricas com Micrometer + Prometheus
- [ ] Refatoração para Clean Architecture / DDD** *(só quando o básico estiver sólido)*

---

## 🛠️ STACK TÉCNICA — CHECKLIST DE DEPENDÊNCIAS

> **Regra:** Marcar como feito quando estiver **configurado e funcionando**.

### Core
- [x] Spring Boot 3.5 (config manual `pom.xml`)
- [x] Spring Web (MVC)
- [ ] Spring Data JPA
- [x] Spring Boot DevTools
- [ ] Spring Boot Starter Validation
- [ ] Spring Boot Starter Test
- [ ] Spring Testes (usar Testcontainers para integração e biblioteca Java Faker para dados de teste)

### Banco de Dados & Migração
- [x] PostgreSQL (runtime)
- [x] Flyway
- [ ] H2 **APENAS para testes de unidade** *(nunca para integração — usar Testcontainers)*

### Mapeamento & Utilitários
- [x] MapStruct
- [x] Lombok

### Segurança
- [ ] Spring Security
- [ ] JWT (jjwt-api, jjwt-impl, jjwt-jackson)

### Documentação
- [ ] SpringDoc OpenAPI (Swagger UI automático)

### Logs
- [ ] Log4j2 **ou** Logback com encoder JSON (logstash-logback-encoder)

### Mensageria
- [ ] Spring AMQP (RabbitMQ)

### Testes
- [ ] JUnit 5
- [ ] Mockito
- [ ] Testcontainers (PostgreSQL module)
- [ ] RestAssured **ou** MockMvc para testes de integração

### DevOps & Qualidade
- [ ] Docker + Docker Compose
- [ ] GitHub Actions
- [ ] SonarQube Community (Docker)

### Ordem de implementação
1.[x] CRUD de marca + testes unitários e talvez de integração
2.[ ] CRUD de produto + testes unitários e talvez de integração
3.[ ] CRUD de variação + testes unitários e talvez de integração
4.[ ] CRUD de estoque + testes unitários e talvez de integração
5.[ ] Endpoint de baixa de estoque
6.[ ] Simular mensageria (mesmo que fake)

---

## 🏗️ ARQUITETURA — EVOLUÇÃO PLANEJADA

```
FASE 1 (Semanas 1-4): Arquitetura em Camadas (Camadaada)
├── controller
├── service
├── repository
├── entity
├── dto
├── exception
└── config

FASE 2 (Semana 7+): Clean Architecture / DDD
├── domain (entities, value objects, domain services)
├── application (use cases, DTOs, mappers)
├── infrastructure (repositories, controllers, messaging, config)
└── config (beans, profiles, security)
```

> **💡 Atenção:** Não pular direto pra DDD. Preciso identificar e **sentir a dor** de ter regras de negócio espalhadas pra entender POR QUE separar domain de infrastructure.

---

## 🧪 TESTES — ESTRATÉGIA POR CAMADA

| Tipo | O que testar | Ferramenta | Banco |
|------|-------------|------------|-------|
| **Unitário** | Service (regras de negócio), Mappers, Utils | JUnit + Mockito | Nenhum (mocks) |
| **Integração (API)** | Endpoints HTTP, serialização, validação | MockMvc ou RestAssured | **Testcontainers PostgreSQL** |
| **Integração (Repo)** | Queries customizadas, migrations | @DataJpaTest | **Testcontainers PostgreSQL** |
| **Integração (Mensageria)** | Producer + Consumer | Spring Boot Test + Testcontainers | PostgreSQL + RabbitMQ |

> **⚠️ ERRO COMUM:** Usar H2 para testes de integração. H2 não é PostgreSQL — seu Flyway pode quebrar, tipos JSON/JSONB não existem, e comportamentos de lock são diferentes. **Testcontainers é o caminho.**

---

## 🔐 SEGURANÇA — FLUXO JWT (Semana 7+)

```
POST /auth/login → retorna JWT
|
├── Header: Authorization: Bearer <token>
├── Roles: ROLE_CLIENTE, ROLE_ADMIN
└── Endpoints:
    ├── GET /produtos → público
    ├── POST /produtos → ROLE_ADMIN
    ├── POST /pedidos → ROLE_CLIENTE (próprio usuário)
    └── GET /estoque/alertas → ROLE_ADMIN
```

---

## 📊 LOGS ESTRUTURADOS — FORMATO ESPERADO

```json
{
  "timestamp": "2026-04-27T21:19:00.000Z",
  "level": "INFO",
  "logger": "c.h.e.service.ProdutoService",
  "message": "Produto criado",
  "thread": "http-nio-8080-exec-1",
  "traceId": "abc123",
  "context": {
    "produtoId": 42,
    "nome": "Shampoo Hidratante",
    "usuario": "admin@loja.com"
  }
}
```

> Use **MDC (Mapped Diagnostic Context)** para propagar `traceId` entre camadas.

---

## 🚀 DEPLOYMENT — CHECKLIST

- [ ] `Dockerfile` multistage (build → runtime JRE slim)
- [ ] `docker-compose.yml` com app + PostgreSQL + RabbitMQ
- [ ] `docker-compose.override.yml` para dev (volumes, ports expostos)
- [ ] GitHub Actions: `mvn test` → `mvn verify` (integração) → build Docker → push
- [ ] Variáveis de ambiente externas (nunca commit `application-prod.yml` com senhas)

