#!/bin/sh
winpty docker exec -it server-ansible //bin//sh -c 'docker run --rm -it wengle/docker-build-code-stage2 //bin//sh'