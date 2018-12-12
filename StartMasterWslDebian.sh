#!/bin/sh
docker run --privileged --name=server-ansible --rm -it --network="bridge" \
    -v "$(pwd):/data":ro \
    wengle/ansible-dind-playbook  \
        ./scripts/StartDockerDaemonAndAnsible.sh  \
        -i /data/ansible/playbooks/inventories/hosts-docker.ini \
        /data/ansible/playbooks/site.yml