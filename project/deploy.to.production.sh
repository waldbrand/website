#!/bin/bash

set -e

./gradlew clean war

rsync -av web/build/libs/waldbrand-website-web-0.0.1-production.war \
root@waldbrand-app.de:/opt/tomcat/tomcat-secure/rootapps/waldbrand/ROOT.war
