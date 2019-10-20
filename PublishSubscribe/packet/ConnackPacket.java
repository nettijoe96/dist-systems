/*
 * ACK for connect packet
 */

package packet;

public class ConnackPacket extends Packet{

    static final String CONNACK = "CONNACK";
    
    public int clientId;   

    public ConnackPacket( ConnectPacket connectPacket, int id){
        super( CONNACK );
        this.clientId = id; 
    }


}
