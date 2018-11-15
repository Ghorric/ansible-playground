#!/bin/sh
winpty docker exec -it server-ansible //bin//sh -c 'docker exec -it server-broker //bin//sh'