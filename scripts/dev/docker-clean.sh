#!/bin/bash

docker system prune -f
docker image prune -f
docker volume prune -f
