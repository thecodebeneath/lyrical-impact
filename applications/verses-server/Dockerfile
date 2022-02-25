FROM eclipse-temurin:11-jre
EXPOSE 8282
ARG JAR_FILE=target/default-app.jar
WORKDIR /app
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
