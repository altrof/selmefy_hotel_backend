FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine
ADD build/libs/hotel-0.0.1-SNAPSHOT.jar /app/app.jar
CMD java -Dspring.config.location=classpath:/application.properties,file:/app/application.properties -jar /app/app.jar
