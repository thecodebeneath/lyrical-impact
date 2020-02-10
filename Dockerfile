FROM adoptopenjdk/openjdk10:jdk-10.0.2.13-alpine
EXPOSE 8080
WORKDIR /app
ADD target/lyrical-impact-0.9-SNAPSHOT.jar .
CMD java -jar lyrical-impact-0.9-SNAPSHOT.jar
