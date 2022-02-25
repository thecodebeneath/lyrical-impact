# Helm managed deployment

This chart installs the [Lyrical Impact](https://github.com/thecodebeneath/lyrical-impact) application.

``` console
cd ./k8s/helm/lyrical-impact

helm install lyrical-impact ./
helm list
helm uninstall lyrical-impact ./
```

## Configuration
The keycloak realm import is done via a generate configmap script in the ./k8s/configmaps directory. This must be done before Helm deployment.

``` console
cd ./k8s/configmaps

ensure previously exported keycloak realm is present "./docker/vols/keycloak-lyrical-realm.json".

run the shell script to create the k8s yaml file in the "./k8s/helm/lyrical-impact/templates" directory
./create-configmap-keycloak.sh
```

### Secrets

All secrets are generated locally from shell scripts in the ./k8s/secrets directory. This must be done before Helm deployment.

``` console
cd ./k8s/secrets

copy and rename "template-secret-*.properties" files to remove the "template-" prefix
edit "secret-*.properties" files to have actual values

run each shell script to create the k8s yaml files in the "./k8s/helm/lyrical-impact/templates" directory
./create-secret-db-app.sh
./create-secret-db.sh
./create-secret-keycloak.sh
./create-secret-mvc.sh
```

### Labels

All Kubernetes resources are labeled with the following:

- app=lyrical-impact

### Environment Variables

The pods also have the following environment variables:

## Usage

Once installed, you can access the application depending on which environment it was deployed to and changes made to your localhosts file.

## More Info

The project objectives and implementation notes about the app are at the Github link above.