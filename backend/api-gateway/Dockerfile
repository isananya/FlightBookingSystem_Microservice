FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

COPY pom.xml .

COPY api-gateway/pom.xml api-gateway/
COPY api-gateway/src api-gateway/src

RUN mvn -f api-gateway/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8765
ENTRYPOINT ["java", "-jar", "app.jar"]