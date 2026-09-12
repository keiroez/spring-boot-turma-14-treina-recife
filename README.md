# Gestão de Projetos — API REST + Frontend (branch de deploy)

Versão do projeto da **aula extra** preparada para ser **publicada na internet** (Railway, via Docker)
e usada pelos alunos para estudar **frontend chamando backend**.

Diferenças em relação à branch `main`:

| | `main` (aula extra) | esta branch |
|---|---|---|
| Banco | MySQL + Flyway | **H2 em memória** + Flyway |
| Frontend | Thymeleaf (páginas renderizadas no servidor) | **HTML/CSS/JS estático** |
| Backend | API REST + MVC | **API REST pura, stateless (JWT)** |
| Swagger | 1 lista só | **2 grupos**: CRUD aberto em `/public/...` e CRUD com JWT |
| Deploy | — | **Dockerfile** pronto (veja [DEPLOY.md](DEPLOY.md)) |

As regras de negócio (services, entidades, validações) são as mesmas.

---

## Stack

| Item | Versão / Tecnologia |
|---|---|
| Java | 17 |
| Spring Boot | 4.0.8 |
| Banco | H2 em memória (não precisa instalar nada) |
| Migrations | Flyway (`src/main/resources/db/migration`) |
| Segurança | Spring Security + JWT (auth0 `java-jwt`), stateless |
| Docs | Swagger UI (`/swagger-ui.html`) |
| Frontend | HTML + CSS + JavaScript puro (`src/main/resources/static`) |

---

## Como rodar

Abra a **raiz do repositório** (é onde está o `pom.xml`) na IDE e rode a classe
`GestaoApplication`, ou pelo terminal:

```bash
./mvnw spring-boot:run          # Linux / macOS / Git Bash
mvnw.cmd spring-boot:run        # Windows (cmd / PowerShell)
```

A aplicação sobe em **http://localhost:8080**.

| URL | O que é |
|---|---|
| http://localhost:8080/ | Tela de login (frontend) |
| http://localhost:8080/painel.html | Painel, depois de logar |
| http://localhost:8080/swagger-ui.html | Documentação da API |

### Usuário de teste

```
e-mail: aluno@treina.com
senha:  123456
```

> **O H2 é em memória:** o banco vive só enquanto a aplicação está rodando. A cada start
> (ou a cada novo deploy no Railway) o Flyway recria as tabelas e insere de novo o usuário
> de teste, um projeto e duas tarefas de exemplo — tudo o que você cadastrar no meio do
> caminho desaparece. Isso é esperado: é um ambiente de estudo, não de produção.

---

## A API

Cada CRUD está publicado em **duas rotas**, com exatamente as mesmas regras e os mesmos
dados — a diferença é só a autenticação:

| Rota | Token? | Para que serve |
|---|---|---|
| `/public/usuarios`, `/public/projetos`, `/public/tarefas` | **não** | começar o frontend sem se preocupar com login |
| `/usuarios`, `/projetos`, `/tarefas` | **sim** | segundo passo: aprender a mandar o JWT |

Comece pelas rotas `/public/...`: é `fetch` puro, sem cabeçalho nenhum. Quando isso estiver
funcionando, troque para as rotas sem o prefixo e aprenda a enviar o token.

| Método | Rota pública (sem token) | Rota privada (com token) |
|---|---|---|
| `GET` `POST` | `/public/usuarios` | `/usuarios` |
| `GET` `PUT` `DELETE` | `/public/usuarios/{id}` | `/usuarios/{id}` |
| `GET` `POST` | `/public/projetos` | `/projetos` |
| `GET` `PUT` `DELETE` | `/public/projetos/{id}` | `/projetos/{id}` |
| `GET` `POST` | `/public/tarefas` | `/tarefas` |
| `GET` `PUT` `DELETE` | `/public/tarefas/{id}` | `/tarefas/{id}` |
| `POST` | `/auth/login` — é onde você **pega** o token | — |
| `POST` | `/auth/register` | — |

> As rotas `/public/...` são **abertas de verdade**: qualquer pessoa com o link pode criar,
> editar e excluir. É de propósito, para o ambiente de estudo. Não cadastre nada real ali.

### Começando sem token

```js
// nenhum cabeçalho, nenhum login
const tarefas = await fetch("/public/tarefas").then(r => r.json());
```

```bash
curl http://localhost:8080/public/tarefas
```

### Pegando o token na mão

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"aluno@treina.com","senha":"123456"}'
```

Resposta: `{"token":"eyJhbGciOi...","email":"aluno@treina.com"}`

```bash
curl http://localhost:8080/usuarios -H "Authorization: Bearer eyJhbGciOi..."
```

Sem o token, a resposta é **403**.

### Swagger em dois grupos

No topo do Swagger UI existe o seletor **Select a definition**:

1. **Rotas públicas (sem token)** — abre por padrão. Tem os três CRUDs em `/public/...`
   (sem cadeado nenhum: dá para clicar em *Try it out* e executar na hora) mais
   `/auth/login` e `/auth/register`.
2. **Rotas privadas (exigem JWT)** — os mesmos CRUDs sem o prefixo, todos com cadeado.
   O `/auth/login` aparece **também** aqui, de propósito: você chama o login, copia o
   `token`, clica em **Authorize**, cola e já testa os CRUDs sem trocar de grupo.

---

## Como é o frontend

Não há framework nem build: são arquivos soltos em `src/main/resources/static`, servidos
pelo próprio Spring Boot.

```
static/
├── index.html       # login
├── cadastro.html
├── painel.html      # menu
├── usuarios.html    # CRUD
├── projetos.html    # CRUD
├── tarefas.html     # CRUD
├── css/style.css
└── js/
    ├── api.js       # token + apiFetch (a parte importante)
    ├── login.js
    ├── cadastro.js
    ├── painel.js
    ├── usuarios.js
    ├── projetos.js
    └── tarefas.js
```

O fluxo que vale entender está em `js/api.js`:

1. o login guarda o token no `localStorage`;
2. `apiFetch()` injeta o cabeçalho `Authorization: Bearer <token>` em toda chamada;
3. se a API responde 401/403, limpa o token e volta para a tela de login.

### Rodar só o frontend apontando para a API publicada

Dá para editar o HTML/JS na sua máquina (ex.: extensão *Live Server* do VS Code) usando o
backend que está no Railway. Basta preencher a constante no topo de `js/api.js`:

```js
const API_URL = "https://seu-projeto.up.railway.app";
```

Com `API_URL` vazio, o frontend chama o mesmo servidor que entregou a página — que é o caso
quando você acessa pelo `localhost:8080`. A classe `config/CorsConfig.java` é o que autoriza
o navegador a fazer essa chamada de uma origem diferente.

---

## Publicar na internet

Passo a passo completo no arquivo **[DEPLOY.md](DEPLOY.md)**.
