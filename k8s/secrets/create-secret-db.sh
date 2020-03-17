#!/bin/bash
#
# script to create a local secret.yaml file from a plain text properties file.
#
kubectl create secret generic secret-db --from-env-file=./secret-db.properties --dry-run=true -o yaml | 
kubectl label -f- --local app=lyrical-impact --dry-run -o yaml  > ../secret-db.yaml
