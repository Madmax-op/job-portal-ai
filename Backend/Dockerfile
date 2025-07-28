# ---- Build Stage ----
  FROM maven:3.9.6-eclipse-temurin-21 AS build
  WORKDIR /app
  COPY pom.xml .
  COPY src ./src
  RUN mvn clean package -DskipTests
  
  # ---- Run Stage ----
  FROM openjdk:21-jdk-slim
  WORKDIR /app
  RUN apt-get update && apt-get install -y wget curl && rm -rf /var/lib/apt/lists/*
  COPY --from=build /app/target/job-portal-backend.jar app.jar
  
  # Create upload directory
  RUN mkdir -p /app/uploads/resumes
  
  # Expose port
  EXPOSE 8080
  
  # Run the app
  ENTRYPOINT ["java", "-jar", "app.jar"]
  