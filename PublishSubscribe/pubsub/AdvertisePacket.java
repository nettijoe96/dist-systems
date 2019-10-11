/*
 * The packet used to communicate an advertise event to a broker
 * Needs a topic that is to be advertised
 */

package pubsub;

public class AdvertisePacket extends Packet{
    private Topic topic;

    public AdvertisePacket(){
        super("advertise");
    }
}
