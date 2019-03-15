#!/bin/bash

SCRIPT_DIR=$(dirname "$(readlink -f $0)")
cd ${SCRIPT_DIR}/../../docker

echo "----------------------------------------------"
echo "Add the following hostnames to /etc/hosts:"
echo "----------------------------------------------"
echo "127.0.0.1 broker mongodb1"
echo "127.0.0.2 mongodb2"
echo "----------------------------------------------"
echo "Ensure that vm.max_map_count is large enough."
echo "On system.d based systems run:"
echo "sudo sysctl -w vm.max_map_count=262144"
echo "Or create a file 60-elasticsearch.conf in"
echo "/etc/sysctl.d/ with the content:"
echo "vm.max_map_count=262144"
echo "----------------------------------------------"

docker-compose up -d

cd -
