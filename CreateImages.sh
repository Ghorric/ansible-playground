#!/bin/sh
docker build -t wengle/docker-build-code docker/docker-build-code/
docker save -o ./docker/docker-dind-ansible-playbook/docker-images/docker-build-code.dimg wengle/docker-build-code
docker build -t wengle/docker-karaf-ssh docker/docker-karaf-ssh/
docker save -o ./docker/docker-dind-ansible-playbook/docker-images/docker-karaf-ssh.dimg wengle/docker-karaf-ssh
docker build -t wengle/ansible-dind-playbook docker/docker-dind-ansible-playbook/