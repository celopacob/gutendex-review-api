FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/demo-0.0.1-SNAPSHOT.jar /app/app.jar

RUN echo "##### Container start..."
CMD ["java", "-jar", "app.jar"]