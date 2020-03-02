[![Build Status](https://travis-ci.org/thecodebeneath/lyrical-impact.svg?branch=master)](https://travis-ci.org/thecodebeneath/lyrical-impact)

# lyrical-impact
A webapp that allows people to remember the lyrical verses that have impacted them in some way.

![Screenshot][1]

[1]: /images/screenshot.png

## Docker

### Docker-compose

Service orchestration is using docker-compose, using local Dockerfiles from the dev build:

```
cd docker
docker-compose build
docker-compose up -d
```

### Google jib (maven plugin)

Starting with Spring Boot 2.3.0M2, maven can create the docker images for Boot apps.

```
mvn clean install
mvn spring-boot:build-image -Dmaven.test.skip=true
cd docker
docker-compose -f docker-compose-spring-boot.yml up -d
```

### Spring Boot Java Buildpack (maven plugin)

Google container tools, maven can create the docker images for Boot apps.

```
mvn clean install
mvn jib:dockerBuild
cd docker
docker-compose -f docker-compose-spring-boot.yml up -d
```
