FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

COPY pom.xml .

COPY eureka-server/pom.xml eureka-server/
COPY eureka-server/src eureka-server/src

RUN mvn -f eureka-server/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]