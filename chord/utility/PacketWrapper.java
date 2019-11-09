package utility;


public class PacketWrapper {


    public int destination;
    public Packet packet;
 
    public PacketWrapper( Packet packet, int destination) {
        this.destination = destination;
        this.packet = packet;
    }

    public boolean atDestination(int id) {
        return destination == id;
    }

}
