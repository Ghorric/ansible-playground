# ansible-playground

Ansible builds, installs and runs a small JMS app on a DinD (Docker in Docker) environment. It creates four hosts: Ansible-Master, JMS-Broker and two DemoApp (Karaf) boxes.

## Warning

This project is NOT a Docker example! Don't use Docker like this for production deployments! (Keep them simple, small and immutable)

This code is just me trying out Ansible. So this is not exactly a how-to project. I made many things work somehow, and I might (or might not) improve it in the future.

## Getting Started

The following instructions will run the app with Docker/Ansible. You can also run the JMS app directly on your local computer (without virtualization; Ansible and Docker are not involved at all). More about that in the 'Pure Karaf setup' chapter.

### Prerequisites

I only tested this on Windows. Theoretically, it shouldn't be too difficult to make it runnable on another OS. Everything is executed inside Docker containers except the script that runs 'docker build', 'docker save' and 'docker run' (***Win.sh scripts are Windows only because they use winpty).

You need to install 'Git Bash' and Docker (links in 'Built With' section).

### Installing

Use 'git clone' to get the project from GitHub.

### Run

Go to the project root with Git Bash and run:
```
sh DeployWin.sh
```
Yes, it takes a lot of time! Especially the first time when it downloads all the images and dependencies.

The playbook should pause with this prompt:
```
[pause]
Press Enter to send DemoApp msgs.:
```

Press enter to send the messages. The output shows how many messages have been processed per host:
```
"msg": "{server-karaf01=48, server-karaf02=52}\n"
```

There is one more prompt that keeps the environment running. It allows accessing the different hosts & Karaf instances via the console. Press enter to finish the Ansible playbook (stops everything).

### Explanation

What just happened when I ran the DeployWin.sh script? In short; it builds the docker images and runs the AnsibleMaster docker image, which in turn uses Ansible to: 
- compile the DemoApp code
- create and run 3 virtual hosts
- start the Karaf instances on these boxes and install: JMS-Broker on 1 Karaf instance, DemoApp on 2  Karaf instances
- send messages (using the DemoApp)

## Karaf only setup

Requeired: Gradle, Maven, Java 1.8, Git Bash

Download Karaf & Compile code:
${workspace_loc:/karaf-env} gradle getKaraf unzip
${workspace_loc:/demoapp-res} gradle publishToMavenLocal
${workspace_loc:/karaf-features} gradle publishToMavenLocal
${workspace_loc:/demoapp-root} gradle dlDependencies jar publishToMavenLocal

Start Karaf in Git Bash: 
sh DevRunKarafWin.sh

Install the DemoApp in the Karaf console:
karaf@root()> feature:repo-add mvn:ch.wengle.demoapp/karaf-features/1.0-SNAPSHOT/xml/features
Adding feature url mvn:ch.wengle.demoapp/karaf-features/1.0-SNAPSHOT/xml/features
karaf@root()> feature:install demoAppJmsAll
karaf@root()> demo-app:send -n 20
{root=20}

Additional stuff in the Karaf console:
karaf@root()> feature:install addonDeployedCamelRoute
karaf@root()> ld | grep Message | tail -n 1
00:17:46.148 INFO [Camel (deployedRoute) thread #4 - timer://test]  | INFO | NO_VALUE | Message 2018-11-19 00:17:46
karaf@root()>
karaf@root()> feature:install addonDecorator
karaf@root()> ld | grep Message | tail -n 1
00:18:07.154 INFO [Camel (deployedRoute) thread #4 - timer://test]  | INFO | NO_VALUE | Message 2018-11-19 00:18:07 | DECORATE=SOME_EVENTTEXT_DECORATION!!!
karaf@root()> feature:uninstall addonDecorator
karaf@root()> ld | grep Message | tail -n 1
00:18:31.152 INFO [Camel (deployedRoute) thread #4 - timer://test]  | INFO | NO_VALUE | Message 2018-11-19 00:18:31

## Built With

* [Ansible](https://www.ansible.com/) - Software provisioning, configuration management, and application deployment
* [Docker](https://www.docker.com/) - Operating-system-level virtualization (software containers)
* [Apache Karaf](https://karaf.apache.org/) - OSGi runtime environment
* [Gradle](https://gradle.org/) - Build Tool / Dependency Management
* [Git Bash](https://git-scm.com/) - Git client shell

## License

This project is licensed under the Apache 2.0 License.

