FROM maven:3.8.7-eclipse-temurin-17 AS compile

COPY pom.xml pom.xml
COPY src src

RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-alpine AS package

RUN apk update && apk add bash
COPY --from=compile target/*.jar /app/transport_commands.jar

WORKDIR /app
ADD docker-entry.sh /app/docker-entry.sh
RUN chmod +x /app/docker-entry.sh

ENTRYPOINT ["/app/docker-entry.sh"]