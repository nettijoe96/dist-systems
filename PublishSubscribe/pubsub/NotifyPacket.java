package pubsub;

import java.util.ArrayList;

public class NotifyPacket extends Packet{
    public ArrayList<Event> events;
    public ArrayList<Topic> advertisements;

    public NotifyPacket(ArrayList<Event> events, ArrayList<Topic> advertisements){
        super("NOTIFY");
        this.events = events; 
        this.advertisements = advertisements;
    }
}
