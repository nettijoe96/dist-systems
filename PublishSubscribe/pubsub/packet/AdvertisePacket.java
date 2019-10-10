package packet;

public class AdvertisePacket extends Packet{
    private Topic topic;

    public AdvertisePacket(){
        super("advertise");
    }
}
