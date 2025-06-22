# Multi-stage build for Spring Boot
FROM openjdk:21-jdk-slim AS builder

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy pom.xml and download dependencies (for better caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build application
COPY src ./src
RUN mvn clean package -DskipTests

# List built JAR files to debug
RUN ls -la /app/target/

# Runtime stage
FROM openjdk:21-jdk-slim

WORKDIR /app

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy built JAR from builder stage - use specific name pattern
COPY --from=builder /app/target/QuizClear-*.jar app.jar

# Set executable permissions
RUN chmod +x app.jar

# List files to debug and check Java version
RUN ls -la /app/ && java -version

# Expose port
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8081/actuator/health || exit 1

# Run application
CMD ["java", "-jar", "app.jar"]
