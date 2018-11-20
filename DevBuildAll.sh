cd karaf-env
gradle getKaraf unzip
cd ../demoapp-res
gradle publishToMavenLocal
cd ../karaf-features
gradle publishToMavenLocal
cd ../demoapp-root
gradle dlDependencies jar publishToMavenLocal