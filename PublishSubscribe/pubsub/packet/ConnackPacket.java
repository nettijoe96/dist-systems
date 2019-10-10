package packet;

public class ConnackPacket extends Packet{

    static final CONNACK = "connack";
    
    public ConnackPacket( ConnectPacket connectPacket ){
        super( CONNACK, connectPacket.getPacketUUID() );
    }
}
