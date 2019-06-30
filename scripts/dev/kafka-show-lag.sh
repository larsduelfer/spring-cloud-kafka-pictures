#!/bin/bash

GROUP=$1

if [[ -z $GROUP ]]; then
	echo "Enter consumer group name:"
	read GROUP
fi

kafka-consumer-groups --bootstrap-server localhost:9092 --group $GROUP --describe
