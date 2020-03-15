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
services are not scalable because all ports are exposed to host
```

## Skaffold + Minikube

Download CLIs for Skaffold and Minikube and put each on PATH env var

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

### Deploy app
- Git Bash:
```
eval $(minikube -p minikube docker-env)
mvn clean install
skaffold run
```

### Open browser to app URL
- Git Bash:
```
minikube service mvc-service
```

## Keycloak 8.0.2 (local app admin)

Create a local service for application OIDC login.

```
cd docker
docker-compose up -d keycloak
```

- Open http://localhost:9090 > Admin Console > Log in
- Add "Realm":
  - Name: "lyrical"
- Configure > Create "Client":
  - Client ID: "lyrical"
  - Client Protocol: "openid-connect"
  - Settings > Valid Redirect URIs: "https://lyricalimpact.net:8443/*"
  - Save
- Configure > Add "Roles:
  - Role Name: "li_admin"
  - Role Name: "li_user"
- Manage > Add "Users:
  1. Local admin user
     - Username: "liadmin"
     - Email: "liadmin@gmail.com"
     - First Name: "Keycloak"
     - Last Name: "Admin"
     - Save
     - Attributes > Add
       - Key: "picture" / Value: "https://vignette.wikia.nocookie.net/westworld/images/d/d8/Maeve_Les_Ecorches.jpg/revision/latest/scale-to-width-down/310"
     - Credentials > Set Password
       - Password: ***
       - Temporary: OFF
       - Set Password
     - Role Mapping > Assigned Roles
       - li_admin
  2. Local user
     - Username: "liuser"
     - Email: "liuser@gmail.com"
     - First Name: "Keycloak"
     - Last Name: "User"
     - Save
     - Attributes > Add
       - Key: "picture" / Value: "https://vignette.wikia.nocookie.net/westworld/images/d/dd/Dolores_Chesnut_ep.jpg/revision/latest/scale-to-width-down/310"
     - Credentials > Set Password
       - Password: ***
       - Temporary: OFF
       - Set Password
     - Role Mapping > Assigned Roles
       - li_user
- Manage Sessions > Realm Sessions > Logout all

