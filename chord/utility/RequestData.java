package utility;

public class RequestData extends Packet{
    public String key;
    public int requester;

    public RequestData( String key, int requester ){
        super( "REQUEST DATA", 0 );
        this.key = key;
        this.requester = requester;
    }
}
