FROM openjdk:17-jdk-alpine
MAINTAINER selmefy
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]