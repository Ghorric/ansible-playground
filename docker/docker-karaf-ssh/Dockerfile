FROM hermsi/alpine-sshd

# Python
RUN apk add --no-cache python && \
    python -m ensurepip && \
    rm -r /usr/lib/python*/ensurepip && \
    pip install --upgrade pip setuptools && \
    rm -r /root/.cache

# JDK
RUN apk update && apk upgrade && \
    apk add openjdk8 && \
    mkdir /tmp/tmprt && \
    cd /tmp/tmprt && \
    apk add zip && \
    unzip -q /usr/lib/jvm/default-jvm/jre/lib/rt.jar && \
    apk add zip && \
    zip -q -r /tmp/rt.zip . && \
    apk del zip && \
    cd /tmp && \
    mv rt.zip /usr/lib/jvm/default-jvm/jre/lib/rt.jar && \
    rm -rf /tmp/tmprt /var/cache/apk/* bin/jjs bin/keytool bin/orbd bin/pack200 bin/policytool \
bin/rmid bin/rmiregistry bin/servertool bin/tnameserv bin/unpack200 

# Maven
ENV MAVEN_VERSION 3.5.4
ENV MAVEN_HOME /usr/lib/mvn
ENV PATH $MAVEN_HOME/bin:$PATH

RUN wget http://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  tar -zxvf apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  rm apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  mv apache-maven-$MAVEN_VERSION /usr/lib/mvn

# Java
ENV KARAF_VERSION=4.2.1
RUN wget http://www-us.apache.org/dist/karaf/${KARAF_VERSION}/apache-karaf-${KARAF_VERSION}.tar.gz; \
    mkdir -p /opt/karaf; \
    tar --strip-components=1 -C /opt/karaf -xzf apache-karaf-${KARAF_VERSION}.tar.gz; \
    rm apache-karaf-${KARAF_VERSION}.tar.gz; \
    mkdir /deploy; \
    sed -i 's/^\(felix\.fileinstall\.dir\s*=\s*\).*$/\1\/deploy/' /opt/karaf/etc/org.apache.felix.fileinstall-deploy.cfg

WORKDIR /opt/karaf

COPY ./scripts .

ENV JAVA_HOME=/usr/lib/jvm/default-jvm/

VOLUME ["/deploy"]
EXPOSE 1099 8101 44444 5005 22 61616

COPY external-karaf-features.xml .
RUN echo "stop karaf.." && \
    sh /opt/karaf/bin/start  && \
    /bin/sh -c 'until sh /opt/karaf/bin/client "feature:repo-add file:/opt/karaf/external-karaf-features.xml"; do echo "Try again feature"; done' && \
    sh /opt/karaf/bin/client "feature:repo-list | grep external-karaf-features"  && \
    echo "install camel.." && \
    /bin/sh -c 'until sh /opt/karaf/bin/client "feature:install camel"; do echo "Try again amqDependencies"; done' && \
    echo "install scr.." && \
    /bin/sh -c 'until sh /opt/karaf/bin/client "feature:install scr"; do echo "Try again amqDependencies"; done' && \
    echo "install aries-blueprint.." && \
    /bin/sh -c 'until sh /opt/karaf/bin/client "feature:install aries-blueprint"; do echo "Try again amqDependencies"; done' && \
    echo "install jms.." && \
    /bin/sh -c 'until sh /opt/karaf/bin/client "feature:install jms"; do echo "Try again amqDependencies"; done' && \
    echo "install shell-compat.." && \
    /bin/sh -c 'until sh /opt/karaf/bin/client "feature:install shell-compat"; do echo "Try again amqDependencies"; done' && \
    echo "install activemq-camel.." && \
    /bin/sh -c 'until sh /opt/karaf/bin/client "feature:install activemq-camel"; do echo "Try again amqDependencies"; done' && \
    echo "install activemq.." && \
    /bin/sh -c 'until sh /opt/karaf/bin/client "feature:install activemq"; do echo "Try again amqDependencies"; done' && \
    echo "install activemq-client.." && \
    /bin/sh -c 'until sh /opt/karaf/bin/client "feature:install activemq-client"; do echo "Try again amqDependencies"; done' && \
    echo "install camel-jms.." && \
    /bin/sh -c 'until sh /opt/karaf/bin/client "feature:install camel-jms"; do echo "Try again amqDependencies"; done' && \
    echo "list packages.." && \
    /bin/sh -c 'until sh /opt/karaf/bin/client "list"; do echo "Try again list"; done' && \
    echo "stop karaf.." && \
    sh /opt/karaf/bin/stop
