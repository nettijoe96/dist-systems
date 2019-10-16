package pubsub;

public class PublishPacket extends Packet{
    private Event event;

    public PublishPacket(){
        super("publish");
    }
}

