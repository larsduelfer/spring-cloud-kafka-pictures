#!/bin/bash

#Copied from https://github.com/mcmoe/mssqldocker

# Start SQL Server
/opt/mssql/bin/sqlservr &

# Start the script to create the DB and user
/usr/config/configure-db.sh

# Call extra command
eval $1
