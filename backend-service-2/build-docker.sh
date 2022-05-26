#!/bin/bash
mvn clean install
docker build -t sansoft/backend-service-2:0.0.2 .