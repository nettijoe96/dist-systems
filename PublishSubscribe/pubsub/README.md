The messages that are transmitted across the network are objects of the supertype Packet.
Each kind of packet has it's own Type (e.g. ConnectPacket).

When a client receives a packet it will check its type and take action based on that.

Currently only QoS 1 ( at least once ) is supported.
