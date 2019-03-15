#!/bin/bash
cd message-api/common-api
./gradlew clean build publishToMavenLocal
cd -
cd message-api/user-message-api
./gradlew clean build publishToMavenLocal
cd -
cd message-api/image-message-api
./gradlew clean build publishToMavenLocal
cd -
cd message-api/comment-message-api
./gradlew clean build publishToMavenLocal
cd -
