#!/usr/bin/env bash

DIR="pubsub"

echo "Starting build"


for entry in "$DIR"/*.java
do
    echo "Building $entry"
    javac "$entry"
done


echo "Build complete"
