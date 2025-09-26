FROM openjdk:25-ea-21-jdk-slim-bullseye
WORKDIR /app
COPY target/TaskBackend-0.0.1-SNAPSHOT.jar /app/todo-app.jar
ENTRYPOINT ["java", "-jar", "todo-app.jar"]