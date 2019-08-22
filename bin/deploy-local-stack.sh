#!/usr/bin/env bash

echo "Building database image ..."
docker build -t mysql ./database
echo "Start deploying database ..."
docker-compose -f docker-compose.yml up -d
echo "Database is running. Use stop-local-stack.sh to stop"