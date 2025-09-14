# Multi-stage build for efficient image size
FROM eclipse-temurin:17-jdk-jammy as build

# Set working directory
WORKDIR /app

# Copy pom.xml first for better layer caching
COPY pom.xml .

# Install Maven and build the application
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copy source code
COPY src src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy

# Create non-root user for security
RUN groupadd -r expensetracker && useradd -r -g expensetracker expensetracker

# Set working directory
WORKDIR /app

# Copy the JAR file from build stage
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Change ownership to non-root user
RUN chown -R expensetracker:expensetracker /app
USER expensetracker

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# JVM optimization for containers
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
