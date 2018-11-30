
# Docker + Java Memory
Java (JVM) interrogates OS for memory and CPU and is unaware of Docker allocations
* Pre-Java 8.133
* From Java 9 onwards (and from 8u131+ onwards, backported) there are flags added to the JVM:
  * ```-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap```
* Once aware, no need to specify memory to the JVM
  * ``` -Xms1024m -Xmx2048m ```
* Side-note: JVM still is unaware of docker limited CPUs available

# Docker

## Dockerfile

```
EXPORT ...
ENV JAVA_OPTS="--XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:+PrintFlagsFinal"
ADD ...
```

## Container memory
```
docker run -m 256m -p 8080:8080 acl-msct:latest
```

## Easiest to confirm container memory allocation and usage with:
``` 
> docker stats
CONTAINER ID        NAME                CPU %      MEM USAGE / LIMIT     MEM %      ...
09c4d97970df        romantic_goldwaser   0.13%      125.4MiB / 128MiB   97.96%      ...
```

# Docker Compose

* Memory Limit: A strict upper limit to the amount of memory made available to a container.
* Memory Reservation: This should be set as the bare minimum amount of memory that an application needs to run properly. So it doesn’t crash or misbehave when the system is trying to reclaim some of the memory.

## Version "3", docker-compose.yml
**Note: Does not handle non-swarm mode deployment memory specifications**
```
version: "3"
services:
  lyricimpact:
    build: .
    deploy: <- read as "swarm deployments"
      resources:
        limits:
          memory: 256M
        reservations:
          memory: 256M
      restart_policy:
        condition: on-failure
    ports:
      - "8080:8080"
```

## Version "2.1" docker-compose.yml
Version 2.x does support memory allocations and will for the foreseeable future.
```yml
version: "2.1"
services:
  acl-msct:
    build: .
    mem_limit: 256m
    mem_reservation: 256m
    ports:
      - "8080:8080"
```

# Other commands to inspect container/JVM utilization
```
> docker ps -q | xargs  docker stats --no-stream
> docker top <cid>
> docker exec -it <cid> ps ef
> docker exec -it <cid> top
> docker exec -it <cid> jps | jstat | jmap
```

# Spring Boot Apps
A fairly simple boot app with H2, JPA, actuator, metrics and a few other boot-starters needs "-m 256m"+
How does we estimate/translate our apps to allocate memory to a PCF Java buildpack deployment?
* A typical PCF Tomee buildpack needed 1G+ 

# Helpful Links
* https://royvanrijn.com/blog/2018/05/java-and-docker-memory-limits/
* https://dzone.com/articles/how-to-decrease-jvm-memory-consumption-in-docker-u
