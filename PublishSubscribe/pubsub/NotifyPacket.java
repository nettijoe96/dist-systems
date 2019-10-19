package pubsub;

import java.util.ArrayList;

public class NotifyPacket extends Packet{
    public ArrayList<Event> events;
    public ArrayList<Topic> ads;

    public NotifyPacket(ArrayList<Event> events, ArrayList<Topic> ads){
        super("NOTIFY");
        this.events = events; 
        this.ads = ads;
    }
}
