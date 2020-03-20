#!/bin/bash
#
docker.exe run --rm  --name dcv -v "f:\dev\lyrical-impact\docker":/input pmsipilot/docker-compose-viz render -m image --force docker-compose.yml
