
FROM openjdk:17-jdk-slim
RUN apt-get update

WORKDIR /app

COPY target/Clinica_escola_backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
