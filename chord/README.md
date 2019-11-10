This is an implementation of a Chord-DHT.

## build and run

To compile run from the /chord directory:

`$./build.sh`

The anchor node must be started before any other node, so run 

`$ ./runAnchor.sh`

Then clients can join the network by running

`$ ./runClient <client uuid>`


## CLI commands:

add <filename>  -  this will add a local file to the network 

request <filename>  - this will query for a file and print the file

close  -  this will cleanly exit a node, ctrl-c will cause the network to fail

ids  - shows the ids that this node is currently acting as

files <directory>  - will show the files in the directory on your local node, useful for figuring out where that one file is from inside the client

nodes - only for anchor. This shows the nodes in the network

fingertable - displays fingertable

data - displays all data of a node
