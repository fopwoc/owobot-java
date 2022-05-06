FROM gradle:latest AS build

LABEL MAINTAINER "Ilya Dobryakov <aspirin@govno.tech>"

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar --no-daemon

FROM openjdk:latest
COPY --from=build /home/gradle/src/build/libs/owobot-java-*-all.jar /owobot-java.jar
ENTRYPOINT ["java","-jar","/owobot-java.jar"]
