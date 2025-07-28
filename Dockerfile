# STAGE 1: Build the application using a Maven image
# This stage will compile the code and create the .jar file
FROM maven:3.8.5-openjdk-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml to leverage Docker's layer caching.
# This downloads dependencies only when pom.xml changes.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of your source code
COPY src ./src

# Build the application. We skip tests as they are not needed for the final artifact.
RUN mvn clean package -DskipTests


# STAGE 2: Create the final, lightweight image with only the JRE
# This stage takes the .jar file from the 'builder' stage and runs it.
FROM openjdk:17-jre-slim

# Set the working directory
WORKDIR /app

# Copy the .jar file from the 'builder' stage
# Make sure the jar name matches the artifactId and version in your pom.xml
COPY --from=builder /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application will run on
EXPOSE 8080

# Set the command to run the application
ENTRYPOINT ["java","-jar","app.jar"]
