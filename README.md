[![Build Status](https://travis-ci.org/thecodebeneath/lyrical-impact.svg?branch=master)](https://travis-ci.org/thecodebeneath/lyrical-impact)

# Lyrical Impact
A webapp that allows people to remember the lyrical verses that have impacted them in some way.

## Table of Contents

* [Screenshot](#screenshot)
* [Architecture](#architecture)
* [Docker-compose](#docker-compose)
  * [Google Jib](#google-jib)
  * [Spring Boot Java Buildpack](#spring-boot-java-buildpack)
  * [Plain Dockerfiles](#plain-dockerfiles)
* [Kubernetes](#kubernetes)
  * [Startup](#startup)
  * [Deploy](#deploy)
  * [Access the App](#access-the-app)
* [Keycloak](#keycloak)
  * [Config Overview](#config-overview)
  * [Config Details](manual-setup-docs#keycloak)

## Screenshot

![Screenshot][1]

[1]: /images/screenshot.png

## Architecture

General goals:
1. Run the app stack on a Windows desktop in each of these deployment configs with the same source code:
   - Spring Boot plugin direct on the host
   - Docker run, using basic Dockerfile(s)
   - Docker-compose, development-like mode using basic Dockerfiles(s), in-memory H2 databases
   - Docker-compose, production-like mode using images created various from helper frameworks, mariadb databases
   - Minikube, production-like mode using images created from Google Jib plugin and running with `skaffold dev`
2. Modeled after the design guidance from:
   - https://12factor.net/
   - https://www.appcontinuum.io/

## Localhost Config

When running with docker-compose:
- edit 'hosts' file
  - `127.0.0.1   lyricalimpact.net   keycloak.net`
- docker-compose.yml
  - `KEYCLOAK_HOST_PORT` is the set to actual host IP & port

When running with minikube:
- edit 'hosts' file
  - `192.168.78.119   lyricalimpact.net   keycloak.net`
  - where this IP address is `minikube ip`

## Docker-compose

Service orchestration is using docker-compose in one of several ways (in order of preference):

![Screenshot][2]

[2]: /docker/docker-compose.png

### Google Jib

Google container tools, the maven plugin can create the docker images for Boot apps.

```
mvn clean install
mvn jib:dockerBuild
cd docker
docker-compose up -d
docker-compose up -d --scale tags=3
```

### Spring Boot Java Buildpack

Starting with Spring Boot 2.3.0M2, the maven plugin can create the docker images for Boot apps.

```
mvn clean install
mvn spring-boot:build-image -Dmaven.test.skip=true
cd docker
docker-compose up -d
docker-compose up -d --scale tags=3
```

### Plain Dockerfiles

```
cd docker
docker-compose -f docker-compose-dev.yml build
docker-compose -f docker-compose-dev.yml up -d
services are not scalable because all ports are exposed to host
```

## Kubernetes

Deployment using Minikube + Skaffold. Download CLIs for Skaffold and Minikube and put each on PATH env var.

### Startup
- Powershell (as admin):
```
minikube config set memory 6144
minikube config set disk-size 40000
minikube config set vm-driver hyperv
minikube start
```

- Git Bash:
```
minikube dashboard
```

### Deploy
- Git Bash:
```
eval $(minikube -p minikube docker-env)
mvn clean install
skaffold run
```

### Access the App
- Git Bash:
```
minikube service mvc-service --https=true
```

## Keycloak

Keycloak, v9.0.0, is a local OIDC server. Create a service for local user accounts that can login to the application.

### Config Overview

To automated the creation of a Keycloak realm, client, roles and users, you must:
1. Start Keycloak in its unconfigured state
2. Create the Keycloak resources via the admin UI
3. Export the realm to json file
4. Mount the json file to the docker container so that Keycloak will automatically import the config on startup

### Config Details

[Keycloak Config Details](manual-setup-docs#keycloak)
