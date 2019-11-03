#!/usr/bin/env bash

sudo docker build -t client:latest .

sudo docker run -it client
