## Rodando o projeto localmente

### Requisitos
- Docker
- JDK 21

### Passos

1. Clone o repositório:
```bash
   git clone git@github.com:deisesalless/hair-ecommerce.git
```

2. Abra o projeto na sua IDE (IntelliJ, Eclipse ou VSCode).

3. Configure as variáveis de ambiente:
```bash
   DB_USERNAME=admin
   DB_PASSWORD=admin123
```

4. Suba o banco de dados com Docker:
```bash
   docker compose up
```

5. Execute a aplicação Spring Boot (run).
