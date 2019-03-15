#!/bin/bash
cd services/common
./gradlew clean build publishToMavenLocal
cd -
cd services/config
./gradlew clean build
cd -
cd services/eureka
./gradlew clean build
cd -
cd services/gateway
./gradlew clean build
cd -
cd services/hystrix
./gradlew clean build
cd -
cd services/search-service
./gradlew clean build
cd -
cd services/resize-service
./gradlew clean build
cd -
cd services/storage-service
./gradlew clean build
cd -
cd services/turbine
./gradlew clean build
cd -
cd services/user-service
./gradlew clean build
cd -
