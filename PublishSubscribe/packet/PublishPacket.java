package packet;
import pubsub.Event;

public class PublishPacket extends Packet{
    public Event event;

    public PublishPacket(Event event, int id){
        super("PUBLISH", id);
        this.event = event;
    }
}

