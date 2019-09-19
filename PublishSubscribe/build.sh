#!/usr/bin/env bash

DIR="pubsub"
PACKETDIR="/packet"

echo "Starting build"

javac ./pubsub/packet/Packet.java

for entry in "$DIR$PACKETDIR"/*.java
do
    echo "Building $entry"
    javac "$entry"
done

for entry in "$DIR"/*.java
do
    echo "Building $entry"
    javac "$entry"
done


echo "Build complete"
