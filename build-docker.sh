#!/usr/bin/env bash

set -e
mvn clean install
# mvn -DskipTests=true clean install
cd ./img-to-webp-service
docker build --build-arg JAR_FILE=target/*.jar -t phip1611/img-to-webp-service .
echo "built Docker image 'phip1611/img-to-webp-service'"
