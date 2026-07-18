FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# curl (necessario para o HEALTHCHECK funcionar na imagem JRE)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*


RUN useradd -r appuser

COPY --from=build --chown=appuser:appuser /app/target/*.jar app.jar

# HEALTHCHECK apontando para um endpoint público
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/api/categories || exit 1

USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]