#!/bin/sh
docker inspect --format '{{ .NetworkSettings.IPAddress }}' $1 | xargs -I {} sh /root/scripts/AddHostsFileEntry.sh {} $1