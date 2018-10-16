docker build -t wengle/docker-karaf-ssh docker/docker-karaf-ssh/
docker save -o ./docker/docker-dind-ansible-playbook/docker-karaf-ssh.dimg wengle/docker-karaf-ssh
docker build -t wengle/ansible-dind-playbook docker/docker-dind-ansible-playbook/
sh ./docker/docker-dind-ansible-playbook/scripts/RunServerAnsible.sh sh ./docker/docker-dind-ansible-playbook/scripts/StartDockerDaemonAndAnsible.sh -i "ansible/playbooks/inventories/hosts-docker.ini" "ansible/playbooks/site.yml" $@