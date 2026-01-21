# Use lightweight Java 17 runtime
FROM eclipse-temurin:17-jre

# App directory inside container
WORKDIR /app

# Copy built jar
COPY target/*.jar /app/


# Expose Spring Boot port
EXPOSE 8080

# Run app
ENTRYPOINT ["java", "-jar", "/app/leads-0.0.1-SNAPSHOT.jar"]
