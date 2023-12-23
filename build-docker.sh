#!/usr/bin/env bash

set -euo pipefail

ARG1="${1:-''}"

if [ "$ARG1" = "--ci" ]; then
    echo "Skipping mvn build"
else
    # Build the JAR file.
    mvn -DskipTests=true package
fi

cd ./img-to-webp-service

echo "building Docker image 'phip1611/img-to-webp-service'"
docker build -t phip1611/img-to-webp-service .
echo "built Docker image 'phip1611/img-to-webp-service'"
