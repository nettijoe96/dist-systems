package packet;
import pubsub.Topic;

public class UnsubscribePacket extends Packet{
    private Topic topic;
    

    public UnsubscribePacket( Topic topic, int id){
        super("UNSUBSCRIBE", id);
        this.topic = topic;
    }

    public Topic getTopic(){
        return this.topic;
    }
}
