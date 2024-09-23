
FROM openjdk:11-jdk-slim

WORKDIR /Tenmo

COPY target/Tenmo.jar Tenmo/Tenmo.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar","Tenmo/Tenmo.jar"]