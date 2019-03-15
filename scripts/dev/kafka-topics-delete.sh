#!/bin/bash

kafka-topics --zookeeper localhost:2181 --delete --topic local.user --if-exists
kafka-topics --zookeeper localhost:2181 --delete --topic local.image --if-exists
kafka-topics --zookeeper localhost:2181 --delete --topic local.comment --if-exists
