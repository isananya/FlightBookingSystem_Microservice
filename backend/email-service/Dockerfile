FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

COPY pom.xml .

COPY email-service/pom.xml email-service/
COPY email-service/src email-service/src

RUN mvn -f email-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/email-service/target/email-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9093
ENTRYPOINT ["java", "-jar", "app.jar"]