#!/bin/bash
mvn clean install
docker build -t sansoft/test-kube-backend:0.0.1 .