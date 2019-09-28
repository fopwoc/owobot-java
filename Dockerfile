FROM alpine:3.7
FROM maven:3.6-jdk-11-slim as target
RUN mkdir -p ./
COPY pom.xml pom.xml
WORKDIR ./
COPY src /src/main/java
WORKDIR ./

RUN mvn package

FROM adoptopenjdk/openjdk11:alpine-jre
COPY --from=target /target/*jar-with-dependencies.jar /app.jar
CMD ["java", "-jar", "/app.jar"]
