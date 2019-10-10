package packet;

public class SubscribePacket extends Packet{
    private Topic topic;

    public SubscribePacket( Topic topic ){
        super("subscribe");
        this.topic = topic;
    }

    public Topic getTopic(){
        return this.topic;
    }
}
