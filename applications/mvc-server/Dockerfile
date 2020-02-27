FROM adoptopenjdk/openjdk10:jdk-10.0.2.13-alpine
EXPOSE 8080
ARG JAR_FILE=target/default-app.jar
WORKDIR /app
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
