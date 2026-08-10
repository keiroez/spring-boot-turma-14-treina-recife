# Spring Boot — Turma 14 (Treina Recife)

Repositório de código das aulas de **Spring Boot** da Turma 14 do Treina Recife.

Cada aula tem sua própria pasta com um projeto Maven independente, mostrando a evolução
incremental de uma API REST — o **Gerenciador de Tarefas** — desde o `hello world` até uma
aplicação completa com persistência, documentação e segurança.

---

## Ementa

| Aula | Tema | Status |
|------|------|--------|
| 1 | Intro Spring Boot, HTTP, REST, start do projeto (com Swagger já plugado desde o início) | ✅ `aula_1_introducao_spring_boot/` |
| 2 | Controllers, Requests/Responses, CRUD básico | ✅ `aula_2_controller_requests/` |
| 3 | Camadas (Model / Repository / Service) + Spring Data JPA | ⏳ |
| 4 | Flyway + Swagger/OpenAPI aprofundado (documentação completa dos endpoints) | ⏳ |
| 5 | Spring Security — Autenticação (stateless vs stateful, login, JWT) | ⏳ |
| 6 | Spring Security — Autorização (roles, permissões, proteção de rotas) | ⏳ |
| 7 e 8 | Projeto integrado — API completa do Gerenciador de Tarefas | ⏳ |

> **Aulas 7 e 8:** a definir se será projeto puro ou projeto + tópico extra
> (tratamento de erros global, paginação/filtros, deploy).

---

## Pré-requisitos

- **JDK 21+** (a aula 1 está configurada para Java 25; a aula 2, para Java 21)
- **Maven** — não precisa instalar, cada projeto traz o wrapper (`mvnw` / `mvnw.cmd`)
- Uma IDE com suporte a Spring (IntelliJ IDEA, VS Code + Extension Pack for Java, Eclipse/STS)
- Um cliente HTTP para testar os endpoints (Postman, Insomnia, `curl` ou o próprio Swagger UI)

---

## Como rodar

> ⚠️ **Importante:** abra sempre a **pasta do projeto da aula** (a que contém o `pom.xml`), e
> não a raiz do repositório. Ex.: `aula_2_controller_requests/app`. Se você abrir a raiz, a IDE
> não reconhece o projeto Maven e o botão de *run* não aparece.

Em qualquer uma das opções abaixo a aplicação sobe em **http://localhost:8080**.
Para parar, use o botão de *stop* (quadrado vermelho) da IDE.

### IntelliJ IDEA

1. `File` → `Open...` → selecione a pasta da aula (ex.: `aula_2_controller_requests/app`) → `OK`.
2. Espere a barra de status terminar de baixar as dependências do Maven
   (o ícone do Maven no canto direito para de girar). Na primeira vez pode demorar alguns minutos.
3. Configure o JDK, se ele reclamar: `File` → `Project Structure` → `Project` → `SDK` → escolha o
   JDK 21+ (ou baixe um por ali mesmo em `Download JDK...`).
4. Abra a classe principal (`AppApplication.java` na aula 2, `DemoApplication.java` na aula 1) e
   clique na **seta verde ▶** ao lado do `public class` ou do método `main` → `Run 'AppApplication'`.

Depois da primeira execução, dá para rodar direto pelo botão ▶ da barra superior.
Para rodar os testes: clique com o botão direito na pasta `src/test/java` → `Run 'All Tests'`.

### VS Code

1. Instale o **Extension Pack for Java** (Microsoft) e o **Spring Boot Extension Pack** (VMware).
2. `File` → `Open Folder...` → selecione a pasta da aula (ex.: `aula_2_controller_requests/app`).
3. Aguarde a mensagem *"Java: Ready"* na barra inferior (importação do projeto Maven).
4. Duas formas de executar:
   - Abra a classe principal e clique em **`Run`**, no *code lens* que aparece acima do método `main`; ou
   - Use a aba **`Spring Boot Dashboard`** (ícone do Spring na barra lateral), selecione a
     aplicação e clique em ▶.

Para rodar os testes, use a aba **Testing** (ícone do béquer) na barra lateral.

### Spring Tool Suite (STS) / Eclipse

1. `File` → `Import...` → `Maven` → `Existing Maven Projects` → `Next`.
2. Em `Root Directory`, aponte para a pasta da aula (ex.: `aula_2_controller_requests/app`),
   marque o `pom.xml` encontrado e clique em `Finish`.
3. Espere o *Building workspace* terminar.
4. Clique com o botão direito no projeto → `Run As` → **`Spring Boot App`**
   (no Eclipse sem STS: `Run As` → `Java Application` → escolha a classe principal).

Para rodar os testes: botão direito no projeto → `Run As` → `JUnit Test`.

### Pelo terminal (alternativa)

Não precisa ter o Maven instalado — cada projeto traz o wrapper:

```bash
# Windows
cd aula_2_controller_requests/app
.\mvnw.cmd spring-boot:run

# Linux / macOS
cd aula_2_controller_requests/app
./mvnw spring-boot:run
```

Para rodar os testes:

```bash
./mvnw test
```

---

## Aula 1 — Introdução ao Spring Boot

**Pasta:** `aula_1_introducao_spring_boot/exemplo`

Projeto gerado no [Spring Initializr](https://start.spring.io) com o mínimo necessário para
entender a estrutura de uma aplicação Spring Boot:

- `spring-boot-starter-webmvc` — servidor embarcado e stack web
- Classe principal anotada com `@SpringBootApplication`
- Configuração via `application.yaml`

Conceitos da aula: o que é o Spring Boot, autoconfiguração, starters, ciclo de request/response
HTTP (métodos, status codes, headers) e os princípios de REST.

## Aula 2 — Controllers, Requests/Responses e CRUD

**Pasta:** `aula_2_controller_requests/app`

Primeira versão do Gerenciador de Tarefas, com o CRUD completo em memória (ainda **sem** banco
de dados — os dados se perdem ao reiniciar a aplicação).

```
com.gestaoTarefas.app
├── AppApplication.java
├── controllers
│   ├── TarefaController.java
│   └── requests
│       ├── TarefaRequest.java      # record — dados de entrada
│       └── TarefaResponse.java     # record — dados de saída
└── models
    ├── Tarefa.java
    └── enums
        ├── Prioridade.java         # BAIXA, MEDIA, ALTA
        └── StatusTarefa.java       # PENDENTE, FAZENDO, CONCLUIDA
```

### Endpoints

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/tarefas` | Lista todas as tarefas |
| `GET` | `/tarefas/{id}` | Busca uma tarefa pelo id |
| `POST` | `/tarefas` | Cria uma nova tarefa (nasce como `PENDENTE`) |
| `PUT` | `/tarefas/{id}` | Atualiza uma tarefa existente |
| `DELETE` | `/tarefas/{id}` | Remove uma tarefa |

Exemplo de criação:

```bash
curl -X POST http://localhost:8080/tarefas \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Estudar Spring","descricao":"Rever a aula 2","prioridade":"ALTA"}'
```

### Swagger / OpenAPI

O projeto já inclui o `springdoc-openapi-starter-webmvc-ui`, então com a aplicação no ar:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Spec OpenAPI (JSON):** http://localhost:8080/v3/api-docs

A documentação detalhada dos endpoints (anotações `@Operation`, `@Schema`, exemplos e códigos
de resposta) será aprofundada na **aula 4**.

### Conceitos trabalhados

- `@RestController` e `@RequestMapping`
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`
- `@PathVariable` e `@RequestBody`
- Separação entre **model** (domínio) e **DTOs** de request/response usando `record`
- Enums no domínio

---

## Convenções do repositório

- Uma pasta por aula, no formato `aula_N_assunto/`
- Cada pasta contém um projeto Maven autocontido (com seu próprio `pom.xml` e wrapper)
- O código de cada aula é preservado como está ao fim daquela aula — as evoluções entram na
  pasta da aula seguinte, para que dê para comparar o antes e o depois
