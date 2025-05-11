# Etapa 1: build com Maven
FROM maven:3.9.4-eclipse-temurin-17 as builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: runtime com JDK leve
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/Clinica_escola_backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
