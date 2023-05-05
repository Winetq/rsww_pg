FROM maven:3.8.7-eclipse-temurin-17 AS compile

COPY pom.xml pom.xml
COPY src src

RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-alpine AS package

COPY --from=compile target/*.jar /app/transport_queries.jar

WORKDIR /app

CMD ["java", "-jar", "transport_queries.jar"]