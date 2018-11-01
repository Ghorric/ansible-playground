#!/bin/sh
# Run docker demon
dockerd &
# Run ansible-playbook
ansible-playbook $@ 