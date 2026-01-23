# ===== STAGE 1 - Build da aplicação =====
FROM gradle:9.0-jdk21 AS builder

WORKDIR /app

# Copia apenas arquivos de configuração primeiro (melhora cache)
COPY gradle gradle
COPY gradlew .
COPY build.gradle* settings.gradle* ./

# Baixa dependências
RUN ./gradlew --no-daemon dependencies

# Agora copia o código fonte
COPY src src

# Gera o JAR executável do Spring Boot
RUN ./gradlew --no-daemon clean bootJar -x test


# ===== STAGE 2 - Imagem final de execução =====
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Cria usuário não-root
RUN useradd -r -u 1001 appuser
USER appuser

# Copia apenas o jar gerado
COPY --from=builder /app/build/libs/*.jar app.jar

# Porta padrão do Spring Boot
EXPOSE 8080

# Parâmetros de JVM preparados para container
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
