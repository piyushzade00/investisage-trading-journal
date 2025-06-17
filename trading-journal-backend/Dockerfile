# Stage 1: Build the Spring Boot application
FROM openjdk:17-jdk-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper files to make the build independent of a local Maven installation
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml file to leverage Docker layer caching
COPY pom.xml .

# This step will download all dependencies and build the project.
# By separating it, if only source code changes, this layer can be cached.
RUN ./mvnw dependency:go-offline -B

# Copy the rest of the source code
COPY src ./src

# Build the Spring Boot application into a JAR file
RUN ./mvnw clean install -DskipTests

# Stage 2: Create the final Docker image
FROM eclipse-temurin:17-jre-focal

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar trading-journal-backend.jar

# Expose the port your Spring Boot application runs on (default is 8080)
EXPOSE 8080

# Define the command to run your Spring Boot application
ENTRYPOINT ["java", "-jar", "trading-journal-backend.jar"]