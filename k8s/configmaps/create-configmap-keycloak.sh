#!/bin/bash
#
# script to create a local configmap.yaml file from a plain text json file.
#
kubectl create configmap configmap-keycloak-import --from-file=../../docker/vols/keycloak-lyrical-realm.json --dry-run=client -o yaml | 
kubectl label -f- --local app=lyrical-impact --dry-run=client -o yaml  > ../helm/lyrical-impact/templates/configmap-keycloak-import.yaml
