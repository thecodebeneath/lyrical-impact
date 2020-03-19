#!/bin/bash
#
# script to create a local configmap.yaml file from a plain text json file.
#
kubectl create configmap configmap-keycloak-import --from-file=../../docker/vols/keycloak-lyrical-realm.json --dry-run=true -o yaml | 
kubectl label -f- --local app=lyrical-impact --dry-run -o yaml  > ../configmap-keycloak-import.yaml
