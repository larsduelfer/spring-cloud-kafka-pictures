#!/bin/bash

kafka-consumer-groups --bootstrap-server localhost:9092 --topic local.user --reset-offsets --group local.storage --to-earliest --execute
