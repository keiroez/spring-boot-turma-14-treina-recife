# Gestão de Projetos — API REST (branch de deploy)

Versão do projeto da **aula extra** reduzida a uma **API REST** e preparada para ser
**publicada na internet** (Railway, via Docker). A ideia é você subir esta API e escrever
o **seu** frontend consumindo ela.

Diferenças em relação à branch `main`:

| | `main` (aula extra) | esta branch |
|---|---|---|
| Banco | MySQL + Flyway | **H2 em memória** + Flyway |
| Frontend | Thymeleaf + JS servidos pela aplicação | **nenhum** — só a API; o frontend é você quem escreve |
| Backend | API REST + MVC | **API REST pura, stateless (JWT)** |
| Rotas | só com autenticação | **`/public/...` aberta** + as mesmas rotas com JWT |
| Swagger | 1 lista só | **2 grupos**: CRUD aberto e CRUD com JWT |
| Deploy | — | **Dockerfile** pronto (veja [DEPLOY.md](DEPLOY.md)) |

As regras de negócio (services, entidades, validações) são as mesmas.

**Índice:** [Stack](#stack) · [Como rodar](#como-rodar) · [A API](#a-api) ·
[Corpo das requisições](#corpo-das-requisições) · [Erros](#erros) ·
[Chamando a API do seu frontend](#chamando-a-api-do-seu-frontend) ·
[Estrutura do projeto](#estrutura-do-projeto) · [Publicar na internet](#publicar-na-internet)

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
| http://localhost:8080/swagger-ui.html | Documentação da API — o ponto de partida |
| http://localhost:8080/ | redireciona para o Swagger |
| http://localhost:8080/public/tarefas | JSON das tarefas, sem login — bom teste rápido |

### Usuário de teste

```
e-mail: aluno@treina.com
senha:  123456
```

> **O H2 é em memória:** o banco vive só enquanto a aplicação está rodando. A cada start
> (ou a cada novo deploy no Railway) o Flyway recria as tabelas e insere de novo o usuário
> de teste, um projeto e duas tarefas de exemplo — tudo o que você cadastrar no meio do
> caminho desaparece. Isso é esperado: é um ambiente de estudo, não de produção.

Na sua máquina não precisa configurar nada: o `application.yml` já tem valores padrão para
o banco e para o segredo do JWT. O token vale **2 horas** (`api.security.token.expiration-hours`).

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

Sem o token, a resposta é **401** com um JSON explicando o que falta.

### Swagger em dois grupos

No topo do Swagger UI existe o seletor **Select a definition**:

1. **Rotas públicas (sem token)** — abre por padrão. Tem os três CRUDs em `/public/...`
   (sem cadeado nenhum: dá para clicar em *Try it out* e executar na hora) mais
   `/auth/login` e `/auth/register`.
2. **Rotas privadas (exigem JWT)** — os mesmos CRUDs sem o prefixo, todos com cadeado.
   O `/auth/login` aparece **também** aqui, de propósito: você chama o login, copia o
   `token`, clica em **Authorize**, cola e já testa os CRUDs sem trocar de grupo.

---

## Corpo das requisições

O que mandar no `POST`/`PUT` de cada recurso. Os exemplos abaixo são JSON válido —
dá para copiar e colar no Postman ou no Swagger.

### Usuário — `/public/usuarios` ou `/usuarios`

```json
{
  "nome": "Maria Silva",
  "cpf": "12345678901",
  "email": "maria@exemplo.com",
  "senha": "123456",
  "dataNascimento": "2000-05-20",
  "status": "ATIVO"
}
```

| Campo | Obrigatório | Regra |
|---|---|---|
| `nome` | sim | — |
| `cpf` | sim | 11 a 14 caracteres, não pode repetir |
| `email` | sim | e-mail válido, não pode repetir |
| `senha` | sim | mínimo 6 caracteres |
| `dataNascimento` | não | data no passado |
| `status` | sim | `ATIVO`, `INATIVO` ou `BLOQUEADO` |

### Projeto — `/public/projetos` ou `/projetos`

```json
{
  "nome": "Site novo",
  "descricao": "texto livre",
  "dataInicio": "2026-02-01",
  "dataConclusao": null,
  "status": "ATIVO",
  "responsavelId": 1
}
```

| Campo | Obrigatório | Regra |
|---|---|---|
| `nome` | sim | — |
| `descricao` | não | — |
| `dataInicio` | sim | — |
| `dataConclusao` | não | — |
| `status` | sim | `ATIVO`, `CONCLUIDO` ou `CANCELADO` |
| `responsavelId` | sim | id de um usuário existente |

### Tarefa — `/public/tarefas` ou `/tarefas`

```json
{
  "titulo": "Criar a tela",
  "descricao": "texto livre",
  "dataConclusao": null,
  "prioridade": "ALTA",
  "status": "PENDENTE",
  "projetoId": 1,
  "usuarioId": 1
}
```

| Campo | Obrigatório | Regra |
|---|---|---|
| `titulo` | sim | — |
| `descricao` | não | — |
| `dataConclusao` | não | — |
| `prioridade` | sim | `BAIXA`, `MEDIA` ou `ALTA` |
| `status` | sim | `PENDENTE`, `FAZENDO` ou `CONCLUIDA` |
| `projetoId` | não | id de um projeto existente |
| `usuarioId` | não | id de um usuário existente |

Datas sempre no formato `aaaa-mm-dd`. O `PUT` substitui o recurso inteiro: mande **todos**
os campos obrigatórios, inclusive a `senha` do usuário. Na resposta, a senha nunca é devolvida.

> As duas únicas regras de negócio que podem barrar um cadastro: **e-mail** e **CPF** de
> usuário não podem repetir.

## Erros

**Todo** erro da API sai no mesmo formato, sem exceção — inclusive os de autenticação e os
inesperados. Nunca vem corpo vazio nem página de erro do servidor:

```json
{
  "timestamp": "2026-02-01T10:00:00",
  "status": 404,
  "erro": "Tarefa nao encontrada com id 99"
}
```

Só os erros de **validação** trazem um campo a mais, o `campos`, com uma mensagem por campo
inválido:

```json
{
  "timestamp": "2026-02-01T10:00:00",
  "status": 400,
  "erro": "Erro de validacao",
  "campos": { "titulo": "Titulo e obrigatorio", "prioridade": "Prioridade e obrigatoria" }
}
```

| Status | Quando acontece | Exemplo de `erro` |
|---|---|---|
| `400` | campo obrigatório faltando | `Erro de validacao` (+ `campos`) |
| `400` | valor fora do enum | `Valor invalido para o campo prioridade: URGENTE. Valores aceitos: BAIXA, MEDIA, ALTA` |
| `400` | JSON mal formado | `Corpo da requisicao invalido: confira se o JSON esta bem formado` |
| `400` | `{id}` que não é número | `Parametro id invalido: abc` |
| `400` | e-mail ou CPF repetido | `Ja existe um usuario com o e-mail ...` |
| `401` | rota privada sem token, ou token expirado | `Token ausente, invalido ou expirado. Faca login em POST /auth/login ...` |
| `401` | login com senha errada | `E-mail ou senha invalidos` |
| `401` | login de usuário inativo/bloqueado | `Nao foi possivel autenticar: usuario inativo ou bloqueado` |
| `404` | `{id}` que não existe | `Tarefa nao encontrada com id 99` |
| `404` | URL que não existe | `Rota nao encontrada: /public/nao-existe` |
| `405` | método errado na rota | `Metodo PATCH nao permitido nesta rota` |
| `409` | excluir registro em uso | `Operacao nao permitida: o registro esta em uso ...` |
| `415` | faltou o `Content-Type` | `Envie o cabecalho Content-Type: application/json` |
| `500` | erro inesperado | `Erro interno no servidor` |

Sucesso, para comparação: `200` nas leituras e no `PUT`, `201` no `POST` e `204` no `DELETE`
(este último sem corpo).

Quem monta essas respostas são duas classes:

| Classe | Cobre |
|---|---|
| `exception/GlobalExceptionHandler.java` | tudo que passa pelo Spring MVC |
| `security/ErroSegurancaHandler.java` | os `401`/`403` do Spring Security, que acontecem **antes** do MVC e por isso não chegam ao handler |

---

## Chamando a API do seu frontend

Esta branch **não tem frontend**: é só a API. Crie seu projeto onde preferir (HTML/CSS/JS
puro com o Live Server do VS Code, React, Angular...) e aponte para esta URL.

O CORS já está liberado para qualquer origem em `config/CorsConfig.java`, então dá para
rodar o frontend em `http://127.0.0.1:5500` chamando a API no `localhost:8080`, ou chamando
o deploy do Railway.

### Passo 1 — sem autenticação

```js
const API = "http://localhost:8080";   // ou a URL do Railway

const tarefas = await fetch(`${API}/public/tarefas`).then(r => r.json());

await fetch(`${API}/public/tarefas`, {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({ titulo: "Minha tarefa", prioridade: "BAIXA", status: "PENDENTE" })
});
```

### Passo 2 — com JWT

```js
// 1. login: guarda o token
const { token } = await fetch(`${API}/auth/login`, {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({ email: "aluno@treina.com", senha: "123456" })
}).then(r => r.json());

localStorage.setItem("token", token);

// 2. toda chamada seguinte manda o token no cabeçalho Authorization
const tarefas = await fetch(`${API}/tarefas`, {
  headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
}).then(r => r.json());
```

Se a resposta vier **401**, o token está ausente, expirado (passou das 2 horas) ou foi
gerado com outro `JWT_SECRET` — apague o token guardado e faça login de novo. O corpo da
resposta diz exatamente isso, então vale um `console.log` nele:

```js
if (!resposta.ok) {
  const erro = await resposta.json();
  console.log(erro.status, erro.erro, erro.campos ?? "");
}
```

---

## Estrutura do projeto

```
.
├── Dockerfile                  # receita de build/execução usada pelo Railway
├── .dockerignore
├── DEPLOY.md                   # passo a passo da publicação
├── pom.xml
└── src/main/
    ├── java/com/exemplo/gestao/
    │   ├── config/             # SecurityConfig, OpenApiConfig (grupos), CorsConfig
    │   ├── controller/         # rotas REST (/... e /public/...)
    │   ├── dto/                # records de request e response
    │   ├── exception/          # GlobalExceptionHandler e exceções próprias
    │   ├── model/              # entidades JPA + enums
    │   ├── repository/         # interfaces do Spring Data
    │   ├── security/           # JwtFilter, TokenService, AutenticacaoService
    │   └── service/            # regras de negócio
    └── resources/
        ├── application.yml
        └── db/migration/       # V1 (tabelas) e V2 (dados iniciais)
```

Onde olhar primeiro, por assunto:

| Quero entender... | Arquivo |
|---|---|
| quais rotas são abertas | `config/SecurityConfig.java` |
| como os dois grupos do Swagger são montados | `config/OpenApiConfig.java` |
| como o token é gerado e validado | `security/TokenService.java`, `security/JwtFilter.java` |
| por que o navegador consegue chamar de outra origem | `config/CorsConfig.java` |
| as tabelas e os dados de exemplo | `db/migration/` |

---

## Publicar na internet

Passo a passo completo no arquivo **[DEPLOY.md](DEPLOY.md)**.
