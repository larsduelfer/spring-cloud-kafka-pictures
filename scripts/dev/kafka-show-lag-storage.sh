#!/bin/bash

kafka-consumer-groups --bootstrap-server localhost:9092 --group local.storage --describe
