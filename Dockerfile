FROM openjdk:8-jdk-alpine
EXPOSE 8080
ENV JAVA_OPTS="--XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -XX:+PrintFlagsFinal"
WORKDIR /app
ADD target/lyrical-impact-0.0.2-SNAPSHOT.jar .
CMD java -jar lyrical-impact-0.0.2-SNAPSHOT.jar
