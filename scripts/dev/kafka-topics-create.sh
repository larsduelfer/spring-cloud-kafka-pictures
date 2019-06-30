#!/bin/bash

kafka-topics --zookeeper localhost:2181 --create --topic zipkin --partitions 3 --replication-factor 1 --if-not-exists
kafka-topics --zookeeper localhost:2181 --create --topic local.user --partitions 3 --replication-factor 1 --if-not-exists
kafka-topics --zookeeper localhost:2181 --create --topic local.image --partitions 3 --replication-factor 1 --if-not-exists
kafka-topics --zookeeper localhost:2181 --create --topic local.comment --partitions 3 --replication-factor 1 --if-not-exists
