package utility;

public class RequestData extends Packet{
    public String key;

    public RequestData( String key ){
        super( "REQUEST DATA", 0 );
        this.key = key;
    }
}
