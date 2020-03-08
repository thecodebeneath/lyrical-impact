[![Build Status](https://travis-ci.org/thecodebeneath/lyrical-impact.svg?branch=master)](https://travis-ci.org/thecodebeneath/lyrical-impact)

# lyrical-impact
A webapp that allows people to remember the lyrical verses that have impacted them in some way.

![Screenshot][1]

[1]: /images/screenshot.png

## Architecture

Modeled after the design guidance from:
- https://12factor.net/
- https://www.appcontinuum.io/

## Docker-compose

Service orchestration is using docker-compose in one of several ways (in order of preference):

### Google jib (maven plugin)

Google container tools, maven can create the docker images for Boot apps.

```
mvn clean install
mvn jib:dockerBuild
cd docker
docker-compose up -d
docker-compose up -d --scale tags=3
```

### Spring Boot Java Buildpack (maven plugin)

Starting with Spring Boot 2.3.0M2, maven can create the docker images for Boot apps.

```
mvn clean install
mvn spring-boot:build-image -Dmaven.test.skip=true
cd docker
docker-compose up -d
docker-compose up -d --scale tags=3
```

### Local build and Dockerfiles

```
cd docker
docker-compose -f docker-compose-dev.yml build
docker-compose -f docker-compose-dev.yml up -d
docker-compose -f docker-compose-dev.yml up -d --scale tags=3
```

## Skaffold + Minikube

- Powershell (as admin):
-- `minikube start --vm-driver=hyperv`
- Git Bash:
-- `minikube dashboard`
- Git Bash:
-- `eval $(minikube -p minikube docker-env)`
-- `mvn clean install`
-- `skaffold build`
-- `skaffold dev`
