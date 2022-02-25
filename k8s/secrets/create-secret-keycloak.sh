#!/bin/bash
#
# script to create a local secret.yaml file from a plain text properties file.
#
kubectl create secret generic secret-keycloak --from-env-file=./secret-keycloak.properties --dry-run=client -o yaml | 
kubectl label -f- --local app=lyrical-impact --dry-run=client -o yaml  > ../helm/lyrical-impact/templates/secret-keycloak.yaml
