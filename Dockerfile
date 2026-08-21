# ---- Build stage ----
FROM maven:3.9.9-eclipse-temurin-25 AS build
WORKDIR /app

# Cache dependency layer trước khi copy source
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:25-jre-jammy
WORKDIR /app

# Chạy bằng non-root user cho an toàn
RUN useradd -r -u 1001 spring
USER spring

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]