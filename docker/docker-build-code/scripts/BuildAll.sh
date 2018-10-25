#!/bin/sh
sh ~/BuildGradleProject.sh "karaf-features" "publishToMavenLocal" && \
sh ~/BuildGradleProject.sh "demoapp-res" "publishToMavenLocal" && \
sh ~/BuildGradleProject.sh "demoapp-root" "dlDependencies jar publishToMavenLocal" && \
echo "End build all"