#!/bin/sh
run_master_box()
{
    PTY=$1
    shift;
    $PTY docker run --privileged --name=server-ansible --rm -it --network="bridge" \
        -p 8101:8101 -p 22:22 \
        -v "$(pwd):/data":ro \
        wengle/ansible-dind-playbook "$@"
}

run_master_box 'winpty' ./scripts/StartDockerDaemonAndAnsible.sh  \
    -i //data//ansible//playbooks//inventories//hosts-docker.ini \
    //data//ansible//playbooks//site.yml