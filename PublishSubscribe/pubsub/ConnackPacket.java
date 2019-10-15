/*
 * ACK for connect packet
 */

package pubsub;

public class ConnackPacket extends Packet{

    static final String CONNACK = "connack";
    
    public int clientId;   

    public ConnackPacket( ConnectPacket connectPacket, int id){
        super( CONNACK );
        this.clientId = id; 
    }


}
