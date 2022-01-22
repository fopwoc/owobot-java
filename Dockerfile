FROM gradle:latest AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar --no-daemon

FROM openjdk:latest
COPY --from=build /home/gradle/src/build/libs/owobot-java-*-all.jar /owobot-java.jar
ENTRYPOINT ["java","-jar","/owobot-java.jar"]
