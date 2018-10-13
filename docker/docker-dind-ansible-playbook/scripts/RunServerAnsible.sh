winpty docker run --privileged --name=server-ansible --rm -it --network="host" \
	-p 8101:8101 -p 22:22 \
	-v "$(pwd):/data":ro \
	wengle/ansible-dind-playbook "$@"