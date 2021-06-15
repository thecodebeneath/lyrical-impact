# Lyrical Impact
A webapp that allows people to remember the lyrical verses that have impacted them in some way.

## Table of Contents

* [Keycloak](#keycloak)

## Keycloak

Keycloak, v13.0.1, is a local OIDC server. Create a service for local user accounts that can login to the application.
```
cd docker
docker-compose up -d keycloak
```

### First time manual setup

- Open http://localhost:8080 > Admin Console > Log in
- Add "Realm":
  - Name: "lyrical"
- Configure > Create "Client":
  - Client ID: "lyrical"
  - Client Protocol: "openid-connect"
  - Settings > Valid Redirect URIs: "http://lyricalimpact.net:9090/*"
  - Save
- Configure > Add "Roles:
  - Role Name: "ROLE_ADMIN"
  - Role Name: "ROLE_USER"
- Configure > Client Scopes > roles > Mappers > realm roles:
  - Add to ID token: ON
- Manage > Add "Users:
  1. Local admin user
     - Username: "liadmin"
     - Email: "liadmin@gmail.com"
     - First Name: "Keycloak"
     - Last Name: "Admin"
     - Save
     - Credentials > Set Password
       - Password: ***
       - Temporary: OFF
       - Set Password
     - Role Mapping > Assigned Roles
       - ROLE_ADMIN
     - Attributes > Add
       - Key: "picture" / Value: "https://i.pinimg.com/236x/16/14/d4/1614d4717ae3e19db55917a883098364--westworld-cast-westworld-tv-series.jpg"
       - Add
       - Save
  2. Local user
     - Username: "liuser"
     - Email: "liuser@gmail.com"
     - First Name: "Keycloak"
     - Last Name: "User"
     - Save
     - Credentials > Set Password
       - Password: ***
       - Temporary: OFF
       - Set Password
     - Role Mapping > Assigned Roles
       - ROLE_USER
     - Attributes > Add
       - Key: "picture" / Value: "https://news.berkeley.edu/wp-content/uploads/2016/11/Dolores300.jpg"
       - Add
       - Save
- Manage Sessions > Realm Sessions > Logout all

### Export realm

After the manual setup above:
```
cd docker
docker exec -it 8c3cf2fdf45c opt/jboss/keycloak/bin/standalone.sh -Djboss.socket.binding.port-offset=100 -Dkeycloak.migration.action=export -Dkeycloak.migration.provider=singleFile -Dkeycloak.migration.realmName=lyrical -Dkeycloak.migration.usersExportStrategy=REALM_FILE -Dkeycloak.migration.file=/tmp/keycloak-lyrical-realm.json
Ctrl-C
docker cp 8c3cf2fdf45c:/tmp/keycloak-lyrical-realm.json ./keycloak-lyrical-realm.json
```
### Start and import realm

To start Keycloak and import the realm, client, roles and users:
```
cd docker
docker-compose up -d keycloak
```
