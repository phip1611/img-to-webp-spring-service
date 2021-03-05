#!/bin/zsh

set -e
mvn clean install
cd ./img-to-webp-service
# from: https://spring.io/guides/gs/spring-boot-docker/
mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
docker build -t phip1611/img-to-webp-service .
echo "built Docker image 'phip1611/img-to-webp-service'"
