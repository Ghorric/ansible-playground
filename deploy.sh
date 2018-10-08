docker network create -d bridge net01
docker build -t wengle/docker-karaf-ssh docker/docker-karaf-ssh/
docker save -o ./docker/docker-dind-ansible-playbook/docker-karaf-ssh.dimg wengle/docker-karaf-ssh
docker build -t wengle/ansible-dind-playbook docker/docker-dind-ansible-playbook/
sh ./docker/docker-dind-ansible-playbook/run-docker-dind-ansible-playbook.sh sh ./docker/docker-dind-ansible-playbook/StartDockerDaemonAndAnsible.sh "ansible/playbooks/DockerRun.yml" -i "ansible/inventories/only-localhost.ini"