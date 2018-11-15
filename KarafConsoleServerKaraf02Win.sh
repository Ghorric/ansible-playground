#!/bin/sh
winpty docker exec -it server-ansible //bin//sh -c 'docker exec -it server-karaf02 sh /opt/karaf/bin/client' 