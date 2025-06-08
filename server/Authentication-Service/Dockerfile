FROM maven:3.9.9 as builder
WORKDIR /build
COPY . .
COPY settings.xml /root/.m2/settings.xml
RUN mvn clean package -DskipTests

FROM openjdk:21-slim as runtime
RUN useradd -ms /bin/bash appuser
WORKDIR /app
COPY --from=builder /build/target/authentication-1.0.jar app.jar
EXPOSE 8080
USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]