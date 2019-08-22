#!/usr/bin/env bash
echo "Stopping containers and removing them ..."
#docker rm $(docker stop $(docker ps -a -q --filter ancestor=mysql --format="{{.ID}}"))
docker stop $(docker ps -a -q) ; docker rm $(docker ps -a -q)
docker volume rm $(docker volume ls -qf dangling=true)
echo "Ups..sorry I removed all of the containers ;("