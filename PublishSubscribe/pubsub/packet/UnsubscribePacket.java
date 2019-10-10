package packet;

public class UnsubscribePacket extends Packet{
    private Topic topic;
    

    public UnsubscribePacket( Topic topic ){
        super("unsubscribe");
        this.topic = topic;
    }

    public Topic getTopic(){
        return this.topic;
    }
}
