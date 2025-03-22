
FROM openjdk:22

WORKDIR /app

COPY target/PayMe.jar PayMe.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar","PayMe.jar"]