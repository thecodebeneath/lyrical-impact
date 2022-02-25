#!/bin/bash
#
docker run --rm  --name dcv -v "f:\dev\lyrical-impact\docker":/input pmsipilot/docker-compose-viz render -m image --output-file=lyrical-impact-topology.png --force docker-compose.yml
