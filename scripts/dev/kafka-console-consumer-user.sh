#!/bin/bash

kafka-console-consumer --bootstrap-server localhost:9092 --topic local.user --from-beginning
