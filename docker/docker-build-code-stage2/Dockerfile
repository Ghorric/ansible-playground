FROM wengle/docker-build-code

RUN mkdir -p .m2/repository

COPY ./demoapp-res ./demoapp-res
COPY ./demoapp-root ./demoapp-root
COPY ./karaf-features ./karaf-features

USER root

RUN chown -R gradle:gradle /home/gradle

# USER gradle

#VOLUME ["/root/.m2/repository"]