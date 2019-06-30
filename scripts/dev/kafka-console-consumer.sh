#!/bin/bash

TOPIC=$1

if [[ -z $TOPIC ]]; then
	echo "Enter topic name:"
	read TOPIC
fi

kafka-console-consumer --bootstrap-server localhost:9092 --topic $TOPIC --from-beginning
