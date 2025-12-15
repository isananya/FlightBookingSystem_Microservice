FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

COPY pom.xml .

COPY flight-service/pom.xml flight-service/
COPY flight-service/src flight-service/src

RUN mvn -f flight-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/flight-service/target/flight-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9091
ENTRYPOINT ["java", "-jar", "app.jar"]