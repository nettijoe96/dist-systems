/*
 * This is a packet that establishes QoS 1 for advertise events
 */

package pubsub;

public class AdvertackPacket extends Packet{
    public AdvertackPacket(){
        super("advertack");
    }
}
