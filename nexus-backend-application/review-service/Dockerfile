FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/review-service-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 8093

ENTRYPOINT ["java", "-jar", "app.jar"]

