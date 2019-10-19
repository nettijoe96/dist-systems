/*
 * The packet used to communicate an advertise event to a broker
 * Needs a topic that is to be advertised
 */

package packet;
import pubsub.Topic;

public class AdvertisePacket extends Packet{
    public Topic topic;

    public AdvertisePacket(Topic topic, int id){
        super("ADVERTISE", id);
        this.topic = topic;
    }
}
