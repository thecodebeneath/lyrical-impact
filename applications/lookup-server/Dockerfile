FROM adoptopenjdk/openjdk13:jdk-13.0.2_8-alpine-slim
EXPOSE 8484
ARG JAR_FILE=target/default-app.jar
WORKDIR /app
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
