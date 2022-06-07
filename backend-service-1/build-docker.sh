#!/bin/bash
mvn clean install
docker build -t sansoft/backend-service-1:0.0.3 .