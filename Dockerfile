FROM alpine:edge
RUN apk add --no-cache openjdk14 maven git

RUN git clone https://github.com/ASPIRINmoe/owobot-java && \
    cd owobot-java && \
    mvn package && \ 
    cp target/*jar-with-dependencies.jar /app.jar && \
    cd / && rm -rf /owobot-java /root/.m2 && \
    apk del maven git

CMD ["java", "-jar", "/app.jar"]
