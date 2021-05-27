#!/bin/bash

set -e

ssh root@waldbrand-app.de touch /opt/tomcat/tomcat-secure/rootapps/waldbrand/ROOT.war
