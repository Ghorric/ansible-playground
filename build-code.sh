winpty docker run --rm -it -v "$(pwd)/demoapp-res/:/home/demoapp-res/" -v "/c/Users/Michael Wengle/.m2/repository:/home/.m2/repository" -v "$(pwd)/demoapp-root/:/home/demoapp-root/"  -v "$(pwd)/karaf-features/:/home/karaf-features/" wengle/docker-build-code //bin//sh #sh BuildAll.sh