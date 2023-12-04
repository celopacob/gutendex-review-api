# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# CMD ["mvn", "clean", "package"]
RUN echo "##### Building package..."
# RUN mvn clean package
# RUN --mount=type=cache,target=/Users/marcelopacobahyba/.m2 mvn clean package
# COPY pom.xml .

# COPY pom.xml /app/
# COPY src /app/src
# RUN mvn -f /app/pom.xml clean package


# Copy the application JAR file into the container
RUN echo "##### Copying package into container..."
COPY target/demo-0.0.1-SNAPSHOT.jar /app/app.jar
# COPY --from=builder /app/target/app*.jar /app/app.jar

# Specify the command to run on container start
RUN echo "##### Container start..."
CMD ["java", "-jar", "app.jar"]