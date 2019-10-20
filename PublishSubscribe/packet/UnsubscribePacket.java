package packet;
import pubsub.Topic;

public class UnsubscribePacket extends Packet{
    private Topic topic;
    

    public UnsubscribePacket( Topic topic, int id){
        super("unsubscribe", id);
        this.topic = topic;
    }

    public Topic getTopic(){
        return this.topic;
    }
}
