package pubsub;

public class ConnectPacket extends Packet{

    static final String CONNECT = "connect";

    private String connectionType;

    public ConnectPacket( String connectionType ){
        super( CONNECT );
        this.connectionType = connectionType;
    }

    public String getConnectionType(){
        return this.connectionType;
    }
}
