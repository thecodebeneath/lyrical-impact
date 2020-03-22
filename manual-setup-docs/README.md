# Lyrical Impact
A webapp that allows people to remember the lyrical verses that have impacted them in some way.

## Table of Contents

* [Keycloak](#keycloak)

## Keycloak

Keycloak, v9.0.0, is a local OIDC server. Create a service for local user accounts that can login to the application.
```
cd docker
docker-compose up -d keycloak
```

### First time manual setup

- Open http://localhost:9090 > Admin Console > Log in
- Add "Realm":
  - Name: "lyrical"
- Configure > Create "Client":
  - Client ID: "lyrical"
  - Client Protocol: "openid-connect"
  - Settings > Valid Redirect URIs: "http://lyricalimpact.net:9090/*"
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
