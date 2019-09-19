#!/usr/bin/env bash

DIR="pubsub"
PACKETDIR="packet"

echo "Starting build"

cd $DIR

for entry in "$PACKETDIR"/*.java
do
    echo "Building $entry"
    javac "$entry"
done

cd ..

for entry in "$DIR"/*.java
do
    echo "Building $entry"
    javac "$entry"
done


echo "Build complete"
