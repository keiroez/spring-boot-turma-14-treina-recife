# Spring Boot — Turma 14 (Treina Recife)

Repositório de código das aulas de **Spring Boot** da Turma 14 do Treina Recife.

Cada pasta é um projeto Maven independente e mostra a evolução incremental de uma API REST —
do `hello world` da aula 1 até uma aplicação completa com MySQL, migrations, validações,
autenticação JWT e frontend.

Dois domínios são usados ao longo do curso:

- **Gerenciador de Tarefas** — aulas 1 e 2, para introduzir controllers e CRUD em memória
- **SGP (Sistema de Gestão de Projetos)** — da revisão em diante: `Usuario`, `Projeto` e `Tarefa`,
  com relacionamentos, persistência e segurança

---

## Mapa do repositório

| # | Pasta | Tema | Projeto desenvolvido |
|---|-------|------|----------------------|
| 1 | `aula_1_introducao_spring_boot/exemplo` | Intro ao Spring Boot, HTTP e REST | `demo` — projeto do Initializr, só sobe o servidor |
| 2 | `aula_2_controller_requests/app` | Controllers, Request/Response, CRUD | `app` — Gerenciador de Tarefas em memória |
| — | `revisao_Logica_POO_Spring/revisao` | Revisão de Lógica e POO em Java puro | `revisao` — veículos, interfaces e polimorfismo |
| — | `revisao_aula_1_e_2/sgp` | Revisão das aulas 1 e 2 no domínio do SGP | `sgp` — 3 controllers em memória, primeiros `@Service`/`@Repository` |
| 3 e 4 | `aula_3_camadas_jpa/sgp` | Camadas (Model/Repository/Service) + Spring Data JPA | `sgp` — CRUD das 3 entidades persistindo em H2 |
| 5 e 6 | `aula_5_spring_boot_projeto/sgp` | Projeto integrado — CRUD completo em camadas | `sgp` — Usuário, Projeto e Tarefa com regras no service |
| 7 | `aula_7_spring_boot_mySQL_flyway_validations/sgp` | MySQL, Flyway e Bean Validation | `sgp` — mesmo projeto migrado para MySQL + migrations |
| Extra | `aula_extra_IA/gestao` | Projeto gerado com IA — API + JWT + frontend | `gestao` — API completa com Security, JWT e Thymeleaf |

---

## Pré-requisitos

- **JDK 21+** (a aula 1 está configurada para Java 25; as aulas 2 a 7, para Java 21;
  o projeto da aula extra, para Java 17)
- **Maven** — não precisa instalar, cada projeto traz o wrapper (`mvnw` / `mvnw.cmd`)
- **MySQL 8+** — necessário apenas na **aula 7** e no projeto da **aula extra**
  (as demais usam H2 em memória)
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
4. Abra a classe principal (`SgpApplication.java` nos projetos do SGP, `AppApplication.java` na
   aula 2, `DemoApplication.java` na aula 1) e clique na **seta verde ▶** ao lado do `public class`
   ou do método `main` → `Run`.

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

**Pasta:** `aula_1_introducao_spring_boot/exemplo` · **Projeto:** `demo` · **Java 25 · Spring Boot 4.1.0**

Projeto gerado no [Spring Initializr](https://start.spring.io) com o mínimo necessário para
entender a estrutura de uma aplicação Spring Boot:

- `spring-boot-starter-webmvc` — servidor embarcado e stack web
- Classe principal `DemoApplication` anotada com `@SpringBootApplication`
- Configuração via `application.yaml`

**Conceitos:** o que é o Spring Boot, autoconfiguração, starters, ciclo de request/response
HTTP (métodos, status codes, headers) e os princípios de REST.

---

## Aula 2 — Controllers, Requests/Responses e CRUD

**Pasta:** `aula_2_controller_requests/app` · **Projeto:** `app` (Gerenciador de Tarefas) · **Java 21**

Primeira versão do Gerenciador de Tarefas, com o CRUD completo **em memória** (ainda sem banco
de dados — os dados se perdem ao reiniciar a aplicação, e o `id` é gerado a partir do tamanho da lista).

```
com.gestaoTarefas.app
├── AppApplication.java
├── controllers
│   ├── TarefaController.java       # List<Tarefa> em memória
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

O projeto já inclui o `springdoc-openapi-starter-webmvc-ui` (que segue presente em todos os
projetos seguintes), então com a aplicação no ar:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Spec OpenAPI (JSON):** http://localhost:8080/v3/api-docs

### Conceitos trabalhados

- `@RestController` e `@RequestMapping`
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`
- `@PathVariable` e `@RequestBody`
- Separação entre **model** (domínio) e **DTOs** de request/response usando `record`
- Enums no domínio

---

## Revisão — Lógica e POO em Java

**Pasta:** `revisao_Logica_POO_Spring/revisao` · **Java puro (sem Maven/Spring)**

Projeto de console para retomar os fundamentos antes de avançar para as camadas do Spring.
Modela veículos e seres que se locomovem:

```
src
├── App.java          # main — monta listas polimórficas e itera sobre elas
├── Veiculo.java      # classe abstrata (encapsulamento + método abstrato)
├── Andador.java      # interfaces de comportamento
├── Nadador.java
├── Voador.java
├── Carro.java        # implementações concretas
├── Caminhao.java
├── Aviao.java
├── Barco.java
└── Pato.java         # implementa Nadador, Voador e Andador
```

**Conceitos:** classes e objetos, atributos e métodos, construtores, encapsulamento
(getters/setters com validação, membros `private`/`protected`), herança, classe abstrata,
interfaces, polimorfismo (`List<Veiculo>`, `List<Voador>`), `instanceof` + cast e
tratamento de exceções com `try/catch`.

Para rodar, abra a pasta no VS Code e execute o `App.java`
(ou `javac -d bin src/*.java && java -cp bin App`).

---

## Revisão — Aulas 1 e 2 no domínio do SGP

**Pasta:** `revisao_aula_1_e_2/sgp` · **Projeto:** `sgp` · **Java 21**

Aqui nasce o **SGP (Sistema de Gestão de Projetos)**, o projeto que segue até o fim do curso.
As três entidades do domínio aparecem pela primeira vez, ainda com listas em memória nos
controllers, mas o `pom.xml` já traz **Spring Data JPA + H2** e o projeto já mostra o primeiro
`UsuarioRepository` e `UsuarioService` — a ponte para a aula de camadas.

```
com.treinarecife.sgp
├── controllers        # UsuarioController, ProjetoController, TarefaController
├── models             # Usuario, Projeto, Tarefa
│   ├── dto            # *Request / *Response (records)
│   └── enums          # StatusUsuario, StatusProjeto, StatusTarefa, Prioridade
├── repositories       # UsuarioRepository
└── services           # UsuarioService
```

Rotas: `/usuarios`, `/projetos` e `/tarefas`, cada uma com os cinco verbos do CRUD.

**Enums do domínio:**

| Enum | Valores |
|------|---------|
| `StatusUsuario` | `ATIVO`, `INATIVO`, `BLOQUEADO` |
| `StatusProjeto` | `ATIVO`, `CONCLUIDO`, `CANCELADO` |
| `StatusTarefa` | `PENDENTE`, `FAZENDO`, `CONCLUIDA` |
| `Prioridade` | `BAIXA`, `MEDIA`, `ALTA` |

---

## Aulas 3 e 4 — Camadas e Spring Data JPA

**Pasta:** `aula_3_camadas_jpa/sgp` · **Projeto:** `sgp` · **Java 21 · H2 em memória**

O SGP passa a ter a arquitetura em camadas completa para as três entidades, e os dados saem da
memória do controller para o banco.

```
com.treinarecife.sgp
├── controllers        # recebe a requisição e devolve DTO
├── services           # @Service — regras de negócio, injetado com @Autowired
├── repositories       # interfaces extends JpaRepository<T, Long>
└── models             # @Entity + @Id/@GeneratedValue + @ManyToOne
```

Configuração (`application.yaml`): H2 em memória (`jdbc:h2:mem:tarefasdb`), console H2 habilitado
em http://localhost:8080/h2-console, `ddl-auto: update` e `show-sql: true` para ver o SQL gerado.

**Relacionamentos:** `Projeto` tem um `Usuario responsavel` (`@ManyToOne` + `@JoinColumn`), e
`Tarefa` se liga a `Usuario` e a `Projeto`.

**Conceitos:** separação de responsabilidades entre controller/service/repository, injeção de
dependência (`@Autowired` e via construtor), `@Entity`, `@Id`, `@GeneratedValue`,
`@ManyToOne`/`@JoinColumn` e os métodos prontos do `JpaRepository`
(`findAll`, `getReferenceById`, `save`, `deleteById`).

---

## Aulas 5 e 6 — Projeto integrado (CRUD completo)

**Pasta:** `aula_5_spring_boot_projeto/sgp` · **Projeto:** `sgp` · **Java 21 · Spring Boot 4.0.8**

Consolidação do SGP: CRUD completo de **Usuário**, **Projeto** e **Tarefa** — construído em aula
um recurso por vez (commits *CRUD de Usuario*, *CRUD de Projeto*, *CRUD de Tarefa*).

A diferença principal em relação à aula 3 é onde mora a conversão de dados: agora é o **service**
que recebe o `*Request`, monta a entidade e persiste; o controller só chama o service e devolve
`toDTO()`. Os services também passam a se compor (o `TarefaService` usa `UsuarioService` e
`ProjetoService` para resolver o responsável e o projeto da tarefa).

```java
// UsuarioService — o service recebe o DTO e devolve a entidade salva
public Usuario inserir(UsuarioRequest req) {
    Usuario usuario = new Usuario(req.nome(), req.cpf(), req.email(),
                                  req.senha(), req.dataNascimento(), req.status());
    return usuarioRepository.save(usuario);
}
```

Continua em H2 (`jdbc:h2:mem:sgp`), com o `spring-boot-h2console` e `ddl-auto: update`.

### Endpoints (iguais nas aulas 5 e 7)

| Recurso | Métodos |
|---------|---------|
| Usuários | `GET /usuarios`, `GET /usuarios/{id}`, `POST /usuarios`, `PUT /usuarios/{id}`, `DELETE /usuarios/{id}` |
| Projetos | `GET /projetos`, `GET /projetos/{id}`, `POST /projetos`, `PUT /projetos/{id}`, `DELETE /projetos/{id}` |
| Tarefas | `GET /tarefas`, `GET /tarefas/{id}`, `POST /tarefas`, `PUT /tarefas/{id}`, `DELETE /tarefas/{id}` |

---

## Aula 7 — MySQL, Flyway e validações

**Pasta:** `aula_7_spring_boot_mySQL_flyway_validations/sgp` · **Projeto:** `sgp` · **Java 21 · MySQL 8**

O mesmo SGP da aula 5, agora com banco de verdade, versionamento de schema e validação de entrada.

### O que mudou

- **MySQL** no lugar do H2 (`mysql-connector-j`), apontando para `jdbc:mysql://localhost:3306/sgp14`
- **Flyway** (`spring-boot-starter-flyway` + `flyway-mysql`) — o schema deixa de ser gerado pelo
  Hibernate e passa a viver em migrations versionadas
- `ddl-auto: validate` — o Hibernate agora só **confere** se as entidades batem com o banco
- **Bean Validation** (`spring-boot-starter-validation`) — `@NotBlank` e `@Email` nos DTOs e
  `@Valid` no `@RequestBody` do controller
- Mapeamento explícito nas entidades: `@Column(name = "...")`, `@Enumerated(EnumType.STRING)` e
  `@GeneratedValue(strategy = GenerationType.IDENTITY)`

### Migrations (`src/main/resources/db/migration`)

| Arquivo | Tabela |
|---------|--------|
| `V1__create_table_usuario.sql` | `usuario` — id_usuario, nome, cpf, email, senha, data_nascimento, status |
| `V2__create_table_projeto.sql` | `projeto` + FK `id_usuario` → `usuario` |
| `V3__create_table_tarefa.sql` | `tarefa` + FKs `id_projeto` e `id_usuario` |

### Antes de rodar

Suba um MySQL em `localhost:3306` com usuário `root` / senha `root` e crie o banco:

```sql
CREATE DATABASE sgp14;
```

(ou ajuste `spring.datasource` em `src/main/resources/application.yaml`).
Na primeira execução o Flyway aplica as três migrations automaticamente.

### Exemplo de validação

```bash
curl -X POST http://localhost:8080/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome":"","email":"nao-e-email"}'
# → 400, com as mensagens "Nome é obrigatório" e "E-mail inválido"
```

---

## Aula extra — Projeto com IA

**Pasta:** `aula_extra_IA/gestao` · **Projeto:** `gestao` · **Java 17 · MySQL 8**

Aplicação gerada na aula extra sobre uso de IA no desenvolvimento: a mesma ideia de gestão de
projetos, porém escrita "de uma vez" com todos os tópicos do curso somados — e mais alguns.

- **Spring Security + JWT stateless** (`java-jwt` da auth0) — `TokenService`, `JwtFilter` e
  `AutenticacaoService` (`UserDetailsService`); `Usuario` implementa `UserDetails`
- **MySQL + Flyway** — `V1__criar_tabelas_iniciais.sql` cria `tb_usuario`, `tb_projeto` e `tb_tarefa`
- **Bean Validation** nos DTOs e **`GlobalExceptionHandler`** (`@RestControllerAdvice`) com
  exceções de negócio próprias (`RecursoNaoEncontradoException`, `RegraNegocioException`)
- **Lombok** nos models e **Swagger** configurado via `OpenApiConfig`
- **Frontend Thymeleaf** servido pelo próprio Spring, consumindo a API via `fetch` com o token
  JWT guardado no `localStorage`

| Recurso | Rotas |
|---------|-------|
| Auth (público) | `POST /auth/register`, `POST /auth/login` |
| API (protegida) | `/usuarios`, `/projetos`, `/tarefas` — CRUD completo, exigem `Authorization: Bearer <token>` |
| Páginas | `/login`, `/cadastro`, `/painel`, `/painel/usuarios`, `/painel/projetos`, `/painel/tarefas` |

O banco `gestao14` é criado automaticamente (`createDatabaseIfNotExist=true`).
Detalhes de execução, exemplos de `curl` e a estrutura completa estão no
[README do projeto](aula_extra_IA/gestao/README.md).

---

## Convenções do repositório

- Uma pasta por aula, no formato `aula_N_assunto/`; revisões em `revisao_*/`
- Cada pasta contém um projeto Maven autocontido (com seu próprio `pom.xml` e wrapper)
- O código de cada aula é preservado como está ao fim daquela aula — as evoluções entram na
  pasta da aula seguinte, para que dê para comparar o antes e o depois
