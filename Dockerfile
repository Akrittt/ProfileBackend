# STAGE 1: Build the application using a standard Maven image
# This stage will compile the code and create the .jar file
FROM maven:3.8.5-openjdk-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml to leverage Docker's layer caching.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of your source code
COPY src ./src

# Build the application, skipping the tests
RUN mvn clean package -DskipTests


# STAGE 2: Create the final, robust image with a full JDK
# This stage takes the .jar file from the 'builder' stage and runs it.
# Using a full JDK image ensures all security providers are available, which can fix SSL issues.
FROM eclipse-temurin:17-jdk

# Set the working directory
WORKDIR /app

# Copy the .jar file from the 'builder' stage
# Make sure the jar name matches the artifactId and version in your pom.xml
COPY --from=builder /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application will run on
EXPOSE 8080

# Set the command to run the application, forcing TLSv1.2 for SSL connections
ENTRYPOINT ["java","-Dhttps.protocols=TLSv1.2","-jar","app.jar"]