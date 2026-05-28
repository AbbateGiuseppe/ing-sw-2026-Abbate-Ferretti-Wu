#!/bin/bash
#Finds the path to this directory
cd "$(dirname "$0")" || exit
#Stores the path to the jar
CLIENT_PATH="game files\gc49-1.0-SNAPSHOT-client.jar"
#Sets the system terminal to 130 columns and 35 rows
printf '\e[8;35;130t'
winpty java --enable-native-access=ALL-UNNAMED -jar "$CLIENT_PATH"