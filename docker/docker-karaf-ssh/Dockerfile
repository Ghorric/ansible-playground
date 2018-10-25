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
EXPOSE 1099 8101 44444 22