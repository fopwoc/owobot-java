FROM gradle:latest AS build

LABEL MAINTAINER="Ilya Dobryakov <ilya.dobryakov@icloud.com>"

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar --no-daemon

FROM eclipse-temurin:17-jre-focal
COPY --from=build /home/gradle/src/build/libs/owobot-java-*-all.jar /owobot-java.jar
ENTRYPOINT ["java","-jar","/owobot-java.jar"]
