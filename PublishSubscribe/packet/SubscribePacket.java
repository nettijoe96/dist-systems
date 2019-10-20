package packet;
import pubsub.Topic;

public class SubscribePacket extends Packet{
    private Topic topic;

    public SubscribePacket( Topic topic, int id){
        super("subscribe", id);
        this.topic = topic;
    }

    public Topic getTopic(){
        return this.topic;
    }
}
