#!/bin/bash

docker exec -it mongodb1 /bin/bash -c "mongo storage-service /docker-entrypoint-initdb.d/mongo-rs-init.js"
