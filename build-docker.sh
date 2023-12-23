#!/usr/bin/env bash

set -euo pipefail

# Build the JAR file.
mvn clean package -DskipTests=true

cd ./img-to-webp-service

echo "building Docker image 'phip1611/img-to-webp-service'"
docker build -t phip1611/img-to-webp-service .
echo "built Docker image 'phip1611/img-to-webp-service'"
