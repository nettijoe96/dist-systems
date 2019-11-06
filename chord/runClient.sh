#!/usr/bin/env bash

uuid=$1
docker run -it --net chord-20-net chord-20:client java src.Client $uuid
