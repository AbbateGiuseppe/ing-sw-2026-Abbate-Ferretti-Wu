#!/bin/bash
#Finds the path to this directory
cd "$(dirname "$0")" || exit
#Stores the path to the jar
SERVER_PATH="target\gc49-1.0-SNAPSHOT-server.jar"
#Runs the server
java -jar "$SERVER_PATH"