#!/usr/bin/env bash

anchorIP="172.10.0.2"
sudo docker run -it --net chord-20-net --ip $anchorIP chord-20:anchor java src.Anchor

