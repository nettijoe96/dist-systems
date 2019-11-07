package packet;


public class Message extends Packet{
    public int destinationId;
    public String message;

    public Message( int id, int destinationId, String message ){
        super( "MESSAGE", id );
        this.destinationId = destinationId;
        this.message = message;
    }

}
