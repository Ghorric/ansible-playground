#!/bin/sh
docker inspect --format '{{ .NetworkSettings.IPAddress }}' $1 | xargs -I {} sh /home/scripts/AddHostsFileEntry.sh {} $1