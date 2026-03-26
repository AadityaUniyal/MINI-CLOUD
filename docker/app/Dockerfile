# MiniCloud Backend App Dockerfile
# Based on OpenJDK 21 (matching project Java version)
FROM openjdk:21-jdk-slim

LABEL maintainer="MiniCloud"
LABEL version="1.0"
LABEL description="MiniCloud Backend Infrastructure Management App"

# Set the working directory
WORKDIR /app

# Copy the build artifact (assuming a JAR file is created)
COPY target/*.jar app.jar

# Expose the REST API port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
