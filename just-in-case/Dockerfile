
FROM openjdk:11-jdk-slim

WORKDIR /app

COPY target/Tenmo.jar Tenmo.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar","Tenmo.jar"]