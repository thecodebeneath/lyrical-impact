[![Build Status](https://travis-ci.org/thecodebeneath/lyrical-impact.svg?branch=master)](https://travis-ci.org/thecodebeneath/lyrical-impact)
[![Releases](https://img.shields.io/github/release/thecodebeneath/lyrical-impact)](https://github.com/thecodebeneath/lyrical-impact/releases)
[![Tags](https://img.shields.io/github/v/tag/thecodebeneath/lyrical-impact)](https://github.com/thecodebeneath/lyrical-impact/tags)

# Lyrical Impact
A webapp that allows people to remember the lyrical verses that have impacted them in some way.

## Table of Contents
* [Screenshot](#screenshot)
* [Architecture](#architecture)
* [Docker-compose Deployment](#docker-compose-deployment)
  * [Google Jib](#google-jib)
  * [Spring Boot Java Buildpack](#spring-boot-java-buildpack)
  * [Plain Dockerfiles](#plain-dockerfiles)
* [Kubernetes Deployment](#kubernetes-deployment)
  * [Deploy via Skaffold](#deploy-via-skaffold)
  * [Deploy via Helm](#deploy-via-helm)
  * [Access the App](#access-the-app)
* [Identity Providers](#identity-providers)
  * [Google](#google)
  * [Okta](#okta)
  * [Keycloak](#keycloak)

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
   - Kubernetes (via Docker Desktop) - production-like mode using k8s resource files, images created from the Google Jib plugin and running in a live-reload mode provided by `skaffold dev`
2. The app stack, including several microservices, are modeled after the design guidance from:
   - https://12factor.net/
   - https://www.appcontinuum.io/
3. Use existing social media logins, via OpenID Connect (OICD)/OAuth2, so that I can avoid storage of usernames and passwords

## Localhost Config
When running from IDE or CLI:
- edit 'hosts' file to have aliases for localhost
  - `127.0.0.1   localhost   lyricalimpact.net   keycloak.lyricalimpact.net`

When running from docker-compose, skaffold or helm:
- edit 'hosts' file to have an entry for host's actual IP address
  - `192.168.1.103   lyricalimpact.net   keycloak.lyricalimpact.net`

## Docker-compose Deployment
Service orchestration using `docker-compose` (or the new `docker compose` command) in one of several ways (in order of preference):

![Screenshot][2]

[2]: /docker/lyrical-impact-topology.png

### Google Jib
Google container tools, the maven plugin can create the docker images for Boot apps.
Use case: Production-like stack deployment. Runs with pre-built containers and a MariaDB service.

``` console
  mvn clean install
  mvn jib:dockerBuild
  
  cd docker
  docker-compose up -d
  docker-compose up -d --scale tags=3
```

### Spring Boot Java Buildpack
Starting with Spring Boot 2.3.0M2, the maven plugin can create the docker images for Boot apps.
Use case: Production-like stack deployment. Runs with pre-built containers and a MariaDB service.

``` console
  mvn clean install
  mvn spring-boot:build-image -Dmaven.test.skip=true
  
  cd docker
  docker-compose up -d
  docker-compose up -d --scale tags=3
```

### Plain Dockerfiles
Use case: Fast developer stack deployment. Runs with local directory Dockerfiles and in-memory H2 database services

``` console
  cd docker
  docker-compose -f docker-compose-dev.yml build
  docker-compose -f docker-compose-dev.yml up -d
  
  note: services are not scalable because all service ports are exposed to host for debugging purposes
```

## Kubernetes Deployment
Deployment using Kubernetes and either Skaffold or Helm. Docker Desktop can manage a single-node Kubernetes instance, so enable
that option. Download CLIs for Skaffold, Helm and Kubectl and put each on PATH env var.

### One-Time Config
- Enable the WLS2 option for Docker and then set resource limits. Create, or edit, the file "~/.wslconfig" file to include:
``` console
  [wsl2]
  memory=9GB
  processors=4
```

### Enable the Kubernetes Dashboard module
Reference: https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/

Powershell (as admin):
``` console
  kubectl proxy
```
Browse to: http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/deployment?namespace=default

### Deploy via Skaffold

Powershell:
``` console
  mvn clean install
  skaffold dev
  ctrl+c to undeploy
```

### Deploy via Helm
Reference full instructions in the [Helm README file](k8s/helm/lyrical-impact/README.md)

Powershell:
``` console
  cd ./k8s/helm/lyrical-impact
  helm install lyrical-impact ./
  helm list
  helm uninstall lyrical-impact
```

### Access the App
- The two external accessible services are LoadBalanced and accessible via localhost aliases.
  - Application: http://lyricalimpact.net:9090/
  - Keycloak Admin: http://keycloak.lyricalimpact.net:8080/

## Identity Providers
The app uses OAUTH2/OpenID Connection (OIDC) to allow for flexible user management, externalized from the app itself. There
are three identity provider integrations that I prioritized for working with: Google, Okta and Keycloak.

### Google
Use the Google Cloud Platform developer `Console` > `APIs & Services` to create `Credentials` > `OAuth2.0 Clients`. The 
`Client ID`, `Client Secret` and `Authorized URIs` are made available to the app via externalized env properties.

- https://console.cloud.google.com/

### Okta
Use the Okta developer console to create `Applications` > `Applications`. The 
`Client ID`, `Client Secret` and `Authorized URIs` are made available to the app via externalized env properties.

- https://developer.okta.com/

### Keycloak
Keycloak, v13.0.1, is a local OIDC server. Create a service for local user accounts that can login to the application.
The `Client ID`, `Client Secret` and `Authorized URIs` are made available to the app via externalized env properties.

#### Keycloak Config Overview
To automated the creation of a Keycloak realm, client, roles and users, you must:
1. Start Keycloak in its unconfigured state
2. Create the Keycloak resources via the admin UI
3. Export the realm to json file
4. Mount the json file to the docker container so that Keycloak will automatically import the config on startup

#### Keycloak Config Details
[Keycloak Config Details](manual-setup-docs#keycloak)
