#!/bin/bash
cd "$(dirname "$0")" || exit
SERVER_PATH="target\gc49-1.0-SNAPSHOT-server.jar"
java -jar "$SERVER_PATH"