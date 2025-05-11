
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/Clinica_escola_backend-0.0.1-SNAPSHOT.jar.original app.jar
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
