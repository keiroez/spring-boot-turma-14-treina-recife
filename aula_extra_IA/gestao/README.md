# Gestão de Projetos — API REST com Spring Boot + JWT

Aplicação de exemplo (gerada na aula extra de IA) com **Usuário**, **Projeto** e **Tarefa**,
autenticação **JWT stateless**, **MySQL**, **Flyway** e validações **Jakarta**.

## Stack

| Item          | Versão / Tecnologia                         |
|---------------|---------------------------------------------|
| Java          | 17 (compila com o `JAVA_HOME` da máquina)   |
| Spring Boot   | 4.0.8                                        |
| Banco         | MySQL 8+                                     |
| Migrations    | Flyway (`src/main/resources/db/migration`)  |
| Segurança     | Spring Security + JWT (auth0 `java-jwt`)     |
| Utilitários   | Lombok, Spring Data JPA, Bean Validation    |
| Docs          | Swagger UI (`/swagger-ui.html`)             |

> O prompt original pedia Java 25. A máquina de build usa JDK 17 (`JAVA_HOME`),
> então o `pom.xml` fixa `java.version=17`. Para usar 21/25, instale a JDK,
> aponte `JAVA_HOME` para ela e altere `<java.version>` no `pom.xml`.

## Pré-requisitos

1. MySQL rodando em `localhost:3306` com usuário `root` / senha `root`
   (ajuste em `src/main/resources/application.yml`).
2. O banco `gestao14` é criado automaticamente (`createDatabaseIfNotExist=true`)
   e o Flyway cria as tabelas `tb_usuario`, `tb_projeto`, `tb_tarefa`.

## Como executar

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.
Swagger UI: `http://localhost:8080/swagger-ui.html`.

## Autenticação

Endpoints públicos: `POST /auth/register` e `POST /auth/login`.
Todos os demais exigem o header `Authorization: Bearer <token>`.

### 1. Registrar

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
        "nome": "Ana Souza",
        "cpf": "12345678901",
        "email": "ana@exemplo.com",
        "senha": "senha123",
        "dataNascimento": "1995-04-10",
        "status": "ATIVO"
      }'
```

Resposta: `{ "token": "...", "tipo": "Bearer", "email": "ana@exemplo.com" }`

### 2. Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{ "email": "ana@exemplo.com", "senha": "senha123" }'
```

### 3. Chamar endpoint protegido

```bash
curl http://localhost:8080/usuarios \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

## Endpoints

| Recurso   | Métodos                                                        |
|-----------|---------------------------------------------------------------|
| Auth      | `POST /auth/login`, `POST /auth/register`                     |
| Usuários  | `GET /usuarios`, `GET /usuarios/{id}`, `POST /usuarios`, `PUT /usuarios/{id}`, `DELETE /usuarios/{id}` |
| Projetos  | `GET /projetos`, `GET /projetos/{id}`, `POST /projetos`, `PUT /projetos/{id}`, `DELETE /projetos/{id}` |
| Tarefas   | `GET /tarefas`, `GET /tarefas/{id}`, `POST /tarefas`, `PUT /tarefas/{id}`, `DELETE /tarefas/{id}` |

## Estrutura em camadas

```
com.exemplo.gestao
├── config       → SecurityConfig, OpenApiConfig
├── security     → TokenService, JwtFilter, AutenticacaoService (UserDetailsService)
├── controller   → AuthController, Usuario/Projeto/TarefaController
├── service      → AuthService, Usuario/Projeto/TarefaService (regras + @Transactional)
├── repository   → Spring Data JPA repositories
├── model        → Usuario (UserDetails), Projeto, Tarefa + enums
├── dto          → Request/Response records
└── exception    → GlobalExceptionHandler + exceções de negócio
```
