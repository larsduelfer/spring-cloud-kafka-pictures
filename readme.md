# Documentation
This documentation describes how to setup prerequisites and how to run the the pictures 
application.

## Setup

The application was developed on a linux host with the IntelliJ IDEA Community Edition and therefore
run configuration for this version are available in the git repository. 

### Install prerequisites

#### IntelliJ IDEA Community Edition
IntelliJ isn't a prerequisite, any other IDE can be used to develop the app. 
Preconfigured run configurations are available for this version.
It can be downloaded [here](https://www.jetbrains.com/idea/download)

#### Confluent Platform
Confluent platform contains a CLI for kafka and is used in scripts to manage topics, etc.
It can be downloaded [here](https://docs.confluent.io/current/installation/installing_cp/rhel-centos.html)

#### Java
The app was developed using java 9 but should be compatible with java 8 as well.

## Build
The libraries of the message api depend on each other and are referenced in the apps.
Therefore the order in which they have to be build (and published to local maven repository) 
is crucial.

Run the following scripts in the root directory of the git repository:

1. build-message-api.sh
2. build-services.sh

## Run

### Start Docker Containers

Navigate to the directory "scripts/dev".

To start / stop the identity server run: `./is-up.sh` or `./is-down.sh`.

To start / stop the other services run `./services-up.sh` or `./services-down.sh`.

Some docker volume remains after stopping the containers (few hundred megabytes per run).
To clean up run `./docker-cleanup.sh`.

### Initialize kafka / databases

Navigate to the directory "scripts/dev".

Run the `./init.sh` script.

### Start the applications

#### Start the webapp
Add some background pictures to the directory webapp/src/assets/images/

Expected are pictures with the name `background1.jpg` .. `background5.jpg`.
This can be adjusted in the file src/app/components/dashboard/dashboard.component.ts

Navigate to the webapp directory and run:

`npm install` and `ng serve`

#### Start the backend applications

##### In IntelliJ

There are preconfigured run configurations to use.

##### Run from command line

Start the services as described below.

###### Config server
To start with a filesystem backend run with the parameters `-Dspring.profiles.active=native,file-local`

To start with a local git backend run with the parameters `-Dspring.profiles.active=git-local`

###### Eureka
Start with the parameters `-Dspring.profiles.active=local`

###### User-Service
Start with the parameters `-Dspring.profiles.active=local -Dkafka.transaction.id.prefix.user=user-instance-1-`

###### Storage-Service
Start with the parameters `-Dspring.profiles.active=local -Dkafka.transaction.id.prefix.storage=storage-instance-1-`

###### Resize-Service
Start with the parameters `-Dspring.profiles.active=local -Dkafka.transaction.id.prefix.resize=resize-instance-1-`

###### Search-Service
Start with the parameters `-Dspring.profiles.active=local -Dkafka.transaction.id.prefix.search=search-instance-1-`

###### Comment-Service
Start with the parameters `-Dspring.profiles.active=local -Dkafka.transaction.id.prefix.comment=comment-instance-1-`

###### Gateway
Start with the parameters `-Dspring.profiles.active=local -Dlogging.level.root=DEBUG`

## Monitoring
A few tools are added to get some insights, what's happening in the application.

### Zipkin
Zipkin is started as docker container and can be accessed at [http://localhost:9411/](http://localhost:9411/).

### Eureka Dashboard
The eureka dashboard can be access at [http://localhost:10010/](http://localhost:10010/).

~~Login with eureka:secret~~

### Kafka Topics UI
The topics UI can be accessed at [http://localhost:8000/](http://localhost:8000/).

### ElasticHQ
A management UI to look into the elasticsearch database can be accessed at [http://localhost:5010/](http://localhost:5010/).

### Config Server
The config server can be accessed at `http://localhost:10000/<service-name>/<profile>`
for example [http://localhost:10000/resize-service/kafka-local](http://localhost:10000/resize-service/kafka-local).

Login with config:secret
