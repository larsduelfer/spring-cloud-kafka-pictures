#!/bin/bash

TOPIC=$1
GROUP=$2

if [[ -z $TOPIC ]]; then
	echo "Enter topic name:"
	read TOPIC
fi

if [[ -z $GROUP ]]; then
	echo "Enter consumer group name:"
	read GROUP
fi

echo "Ensure that all affected consumers are disconnected - otherwise the operation will fail."

kafka-consumer-groups --bootstrap-server localhost:9092 --topic $TOPIC --reset-offsets --group $GROUP --to-earliest --execute
