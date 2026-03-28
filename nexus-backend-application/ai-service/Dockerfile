# Build stage
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy parent POM
COPY pom.xml ./
COPY ai-service/pom.xml ./ai-service/

# Copy source code
COPY ai-service/src ./ai-service/src

# Build the application
RUN mvn clean package -pl ai-service -am -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/ai-service/target/*.jar app.jar

# Expose port
EXPOSE 8089

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8089/actuator/health || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]

