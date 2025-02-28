# Use an official Maven image as the base image
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build the device application JAR
RUN mvn clean package -DskipTests

# Use an official OpenJDK image as the base image
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copy the device service JAR from the build stage
COPY --from=build /app/target/Chat.jar app.jar
# Expose port 8081 for the device service
EXPOSE 8084
CMD ["java", "-jar", "app.jar"]