# =============================================================================
# Dockerfile - receita para o Railway (ou qualquer servidor) montar e rodar
# a aplicacao. Sao duas etapas ("multi-stage"):
#   1) uma imagem com Maven + JDK, que compila o projeto e gera o .jar
#   2) uma imagem so com a JRE, que recebe o .jar e executa
# A segunda etapa nao carrega Maven nem codigo-fonte, por isso a imagem final
# fica pequena e sobe rapido.
# =============================================================================

# ---------- Etapa 1: build ----------
FROM maven:3.9-eclipse-temurin-17 AS build

# Pasta de trabalho dentro do container
WORKDIR /app

# Copia o projeto (o .dockerignore evita copiar target/, .git/ etc.)
COPY pom.xml .
COPY src ./src

# Compila e empacota. -DskipTests porque os testes ja rodam na sua maquina/CI.
RUN mvn -B clean package -DskipTests

# ---------- Etapa 2: execucao ----------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Traz apenas o .jar gerado na etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Documenta a porta padrao. No Railway a porta real vem na variavel PORT,
# que o application.yml le em server.port=${PORT:8080}.
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
