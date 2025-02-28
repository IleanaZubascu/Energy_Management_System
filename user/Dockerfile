# Use an official Maven image as the base image
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build the user application JAR
RUN mvn clean package -DskipTests

# Use an official OpenJDK image as the base image
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copy the user service JAR from the build stage
COPY --from=build /app/target/User.jar app.jar
# Expose port 8080 for the user service
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]