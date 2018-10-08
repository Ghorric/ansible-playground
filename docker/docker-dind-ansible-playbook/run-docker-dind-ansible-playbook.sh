winpty docker run --privileged --name=server-ansible --rm -it --network="net01" \
	-p 58101:58101 -p 48101:48101 \
	-v "$(pwd):/data":ro \
	wengle/ansible-dind-playbook "$@" 
#	-v "$(pwd)/ansible/hosts:/etc/ansible/hosts" \