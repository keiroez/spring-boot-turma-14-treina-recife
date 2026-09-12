# Publicando a aplicação no Railway com Docker

Objetivo: sair do `localhost` e deixar a API + frontend rodando em um endereço público,
tipo `https://seu-projeto.up.railway.app`, sem instalar nada além do navegador.

Não é uma aula de Docker — é o mínimo para você entender o que está acontecendo.

---

## 1. Os dois arquivos que fazem a mágica

### `Dockerfile`

O Railway não sabe o que é um projeto Spring Boot. O `Dockerfile` é a **receita** que ensina
qualquer servidor a montar e rodar a aplicação. Ele tem duas etapas:

1. **build** — parte de uma imagem que já tem Maven e JDK 17, copia o `pom.xml` e o `src`,
   roda `mvn clean package` e gera o `target/gestao-0.0.1-SNAPSHOT.jar`;
2. **execução** — parte de uma imagem que tem **só a JRE** (sem Maven, sem código-fonte),
   recebe o `.jar` da etapa anterior e executa `java -jar app.jar`.

Essa separação (“multi-stage”) existe para a imagem final ficar pequena: o que sobe para o
servidor é uma JRE + um `.jar`, não o ambiente de compilação inteiro.

> Uma **imagem** é o pacote pronto (sistema + Java + seu jar). Um **container** é essa imagem
> rodando. O Railway monta a imagem e sobe o container por você.

### `.dockerignore`

Lista o que **não** deve ser copiado para dentro da imagem (`target/`, `.git/`, arquivos da
IDE). Sem ele, você mandaria centenas de MB inúteis para o build.

---

## 2. Por que `server.port: ${PORT:8080}`

Em `application.yml`:

```yml
server:
  port: ${PORT:8080}
```

O Railway decide em qual porta o seu container deve escutar e entrega esse número na variável
de ambiente `PORT`. Se você fixar `8080`, o Railway não consegue rotear o tráfego e o deploy
fica “unhealthy”. A sintaxe `${PORT:8080}` significa: *use a variável `PORT`; se ela não
existir (o caso da sua máquina), use 8080*.

O mesmo vale para o segredo do JWT:

```yml
api:
  security:
    token:
      secret: ${JWT_SECRET:chave-de-desenvolvimento-troque-em-producao}
```

Segredo **nunca** vai chumbado no código que está no GitHub.

---

## 3. Subir o código para o GitHub

O Railway lê o seu repositório, então o código precisa estar publicado:

```bash
git add .
git commit -m "Versao para deploy no Railway"
git push -u origin deploy-railway
```

---

## 4. Criar o projeto no Railway

1. Acesse **https://railway.app** e faça login com a conta do GitHub
   (o plano gratuito/trial é suficiente para estudar).
2. **New Project** → **Deploy from GitHub repo**.
3. Autorize o Railway a acessar seus repositórios (`Configure GitHub App`) e escolha
   o repositório do curso.
4. Em **Settings** → **Source** → **Branch**, selecione a branch **`deploy-railway`**.
   Se você fez fork/clone e usou outro nome, selecione o nome que usou.

O Railway detecta o `Dockerfile` na raiz do repositório sozinho e usa ele para o build —
você não precisa configurar linguagem, versão de Java nem comando de start.

> Se o projeto **não** estiver na raiz, é preciso informar
> **Settings** → **Build** → **Root Directory**. Nesta branch ele está na raiz justamente
> para evitar esse passo.

---

## 5. Configurar a variável de ambiente

1. Abra o serviço criado → aba **Variables** → **New Variable**.
2. Nome: `JWT_SECRET`
   Valor: qualquer texto longo e aleatório, ex.: `troque-isso-por-um-texto-bem-longo-e-aleatorio`.
3. Salve. O Railway reinicia o serviço automaticamente.

Não crie a variável `PORT` — o Railway já injeta essa.

---

## 6. Gerar o endereço público

Por padrão o serviço não tem URL. Vá em **Settings** → **Networking** →
**Generate Domain**. O Railway devolve algo como:

```
https://spring-boot-turma-14-production.up.railway.app
```

---

## 7. Acompanhar o build e validar

Na aba **Deployments** você vê os logs. A sequência esperada:

1. logs do Maven baixando dependências e compilando (`BUILD SUCCESS`);
2. o banner do Spring Boot;
3. `Successfully applied 2 migrations to schema "PUBLIC"` — o Flyway criando as tabelas no H2;
4. `Tomcat started on port XXXXX` e `Started GestaoApplication`.

Com o domínio no ar, teste:

| Teste | O que esperar |
|---|---|
| `https://SEU-DOMINIO/` | tela de login |
| logar com `aluno@treina.com` / `123456` | vai para o painel e lista os dados de exemplo |
| `https://SEU-DOMINIO/swagger-ui.html` | Swagger com os dois grupos |
| `https://SEU-DOMINIO/public/tarefas` | lista as tarefas em JSON, sem token |
| `https://SEU-DOMINIO/usuarios` sem token | `403` |

---

## 8. Atualizar depois

Cada `git push` na branch configurada dispara um novo build e deploy automático.
Como o banco é H2 em memória, **todo deploy zera os dados** e volta ao estado da migration
`V2__dados_iniciais.sql`.

---

## 9. Se der errado

| Sintoma | Causa provável |
|---|---|
| Build falha no Maven | erro de compilação — rode `./mvnw clean package` na sua máquina primeiro |
| Deploy fica “unhealthy” / domínio dá 502 | a porta foi fixada em vez de usar `${PORT:8080}` |
| Login dá 403 depois de um redeploy | token antigo no navegador com `JWT_SECRET` novo — limpe o `localStorage` e logue de novo |
| Frontend local não chama a API | `API_URL` em `js/api.js` sem a URL do Railway, ou digitada com `/` no final |
| “No Dockerfile found” | o `Dockerfile` não está na raiz da branch selecionada |

---

## Rodando com Docker na sua máquina (opcional)

Se você tiver o Docker Desktop instalado, é o mesmo processo do Railway, local:

```bash
docker build -t gestao .
docker run -p 8080:8080 gestao
```

Depois acesse http://localhost:8080. O `-p 8080:8080` liga a porta 8080 da sua máquina
na porta 8080 de dentro do container.
