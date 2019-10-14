/*
 * ACK for connect packet
 */

package pubsub;

public class ConnackPacket extends Packet{

    static final String CONNACK = "connack";
    
    public int clientUUID;   

    public ConnackPacket( ConnectPacket connectPacket, int UUID){
        super( CONNACK );
        this.clientUUID = UUID; 
    }


}
