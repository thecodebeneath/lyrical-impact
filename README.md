[![Build Status](https://travis-ci.org/thecodebeneath/lyrical-impact.svg?branch=master)](https://travis-ci.org/thecodebeneath/lyrical-impact)
[![Releases](https://img.shields.io/github/release/thecodebeneath/lyrical-impact)](https://github.com/thecodebeneath/lyrical-impact/releases)
[![Tags](https://img.shields.io/github/v/tag/thecodebeneath/lyrical-impact)](https://github.com/thecodebeneath/lyrical-impact/tags)

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

## Objectives
1. Create a fun web site that scratches a personal itch. I'm hoping to actually go-live with this.
2. Get personally caught up on modern tech stack, including Spring Boot, JPA, MVC, Eureka/Ribbon/Feign and Docker Compose & Kubernetes

## Architecture
General goals:
1. Be able to run the app stack on a Windows desktop in each of these deployment configs with the same source code:
   - Spring Boot plugin direct on the host (same as java -jar ...)
   - Docker run - using custom/basic Dockerfile(s)
   - Docker-compose - development-like mode using the basic Dockerfiles(s) & in-memory H2 databases
   - Docker-compose - production-like mode using images created from various from helper frameworks & a MariaDB database
   - Minikube - production-like mode using k8s resource files, images created from the Google Jib plugin and running in a live-reload mode with `skaffold dev`
2. The app stack, including several microservices, are modeled after the design guidance from:
   - https://12factor.net/
   - https://www.appcontinuum.io/
3. Use existing social media logins, via OpenID Connect (OICD)/OAuth2, so that I can avoid storage of usernames and passwords

## Localhost Config
When running with docker-compose:
- edit 'hosts' file
  - `127.0.0.1   lyricalimpact.net   keycloak.net`
- docker-compose.yml
  - `KEYCLOAK_HOST_PORT` is the set to actual host IP & port

When running with minikube:
- deploy all resource yamls
- `minikube tunnel`
- `kubectl get service mvc-service`, copy the external IP address
- `kubectl get service keycloak`, copy the external IP address
- edit 'hosts' file
  - `172.17.228.199   lyricalimpact.net`
  - `172.17.228.205   keycloak.net`
- edit `k8s/configmap-base.yaml`
  - `KEYCLOAK_HOST_PORT: http://172.17.228.205:8080`
- rescale deployment `mvc-service` so pod picks up the new configmap

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
minikube config set cpu 4
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
skaffold dev
```

### Access the App
- Git Bash:
```
minikube service mvc-service
```

## Keycloak
Keycloak, v9.0.2, is a local OIDC server. Create a service for local user accounts that can login to the application.

### Config Overview
To automated the creation of a Keycloak realm, client, roles and users, you must:
1. Start Keycloak in its unconfigured state
2. Create the Keycloak resources via the admin UI
3. Export the realm to json file
4. Mount the json file to the docker container so that Keycloak will automatically import the config on startup

### Config Details
[Keycloak Config Details](manual-setup-docs#keycloak)
