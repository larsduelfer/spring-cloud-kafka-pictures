#!/bin/bash
function progress {
	echo -e "\e[1m\e[33m$1...\e[0m"
}

function build_publish {
	DIRECTORY=$1
	cd "$DIRECTORY"
	./gradlew clean build publishToMavenLocal
	cd -
	printf "\n"
}

function build {
	DIRECTORY=$1
	cd "$DIRECTORY"
	./gradlew clean build
	cd -
	printf "\n"
}
