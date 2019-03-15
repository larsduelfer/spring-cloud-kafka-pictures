#!/bin/bash

SCRIPT_DIR=$(dirname "$(readlink -f $0)")
cd ${SCRIPT_DIR}/../../services/identityserver

docker-compose down

cd -
