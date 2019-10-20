package packet;
import pubsub.Topic;

public class SubscribePacket extends Packet{
    private Topic topic;

    public SubscribePacket( Topic topic, int id){
        super("SUBSCRIBE", id);
        this.topic = topic;
    }

    public Topic getTopic(){
        return this.topic;
    }
}
