FROM openjdk:8-jdk-alpine
ENV JAVA_OPTS="--XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=2"
EXPOSE 8080
WORKDIR /app
ADD target/lyrical-impact-0.0.2-SNAPSHOT.jar .
CMD java -jar lyrical-impact-0.0.2-SNAPSHOT.jar
