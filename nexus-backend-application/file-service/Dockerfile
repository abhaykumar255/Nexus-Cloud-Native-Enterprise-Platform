FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the JAR file
COPY target/*.jar app.jar

# Create file storage directories
RUN mkdir -p /app/file-storage /app/sftp-uploads /app/sftp

# Expose the port
EXPOSE 8085
EXPOSE 2222

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8085/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

