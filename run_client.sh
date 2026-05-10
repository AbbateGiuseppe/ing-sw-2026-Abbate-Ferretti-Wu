#!/bin/bash
cd "$(dirname "$0")" || exit
CLIENT_PATH="target\gc49-1.0-SNAPSHOT-client.jar"
winpty java --enable-native-access=ALL-UNNAMED -jar "$CLIENT_PATH"