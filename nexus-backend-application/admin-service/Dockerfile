FROM openjdk:17-jdk-slim as build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apt-get update && apt-get install -y maven && \
    mvn clean package -DskipTests && \
    mv target/*.jar app.jar

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/app.jar app.jar
EXPOSE 8097
ENTRYPOINT ["java", "-jar", "app.jar"]

