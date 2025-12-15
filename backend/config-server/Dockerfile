FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

COPY pom.xml .

COPY config-server/pom.xml config-server/
COPY config-server/src config-server/src

RUN mvn -f config-server/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

RUN apk add --no-cache curl
COPY --from=build /app/config-server/target/config-server-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]