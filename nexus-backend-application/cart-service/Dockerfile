# Multi-stage build for Cart Service
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy parent pom and common-lib first (for layer caching)
COPY pom.xml ./
COPY common-lib ./common-lib

# Copy cart-service
COPY cart-service/pom.xml ./cart-service/
COPY cart-service/src ./cart-service/src

# Build common-lib first, then cart-service
RUN apk add --no-cache maven && \
    mvn clean install -pl common-lib -DskipTests && \
    mvn clean package -pl cart-service -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/cart-service/target/*.jar app.jar

# Run as non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]

