FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

COPY pom.xml .

COPY booking-service/pom.xml booking-service/
COPY booking-service/src booking-service/src

RUN mvn -f booking-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/booking-service/target/booking-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9092
ENTRYPOINT ["java", "-jar", "app.jar"]