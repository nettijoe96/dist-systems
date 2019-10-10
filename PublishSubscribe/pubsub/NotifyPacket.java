package pubsub;

public class NotifyPacket extends Packet{
    private Event event;

    public NotifyPacket(){
        super("notify");
    }
}
