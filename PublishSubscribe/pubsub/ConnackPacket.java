package pubsub;

public class ConnackPacket extends Packet{

    static final String CONNACK = "connack";
    
    public ConnackPacket( ConnectPacket connectPacket ){
        super( CONNACK, connectPacket.getPacketUUID() );
    }
}
