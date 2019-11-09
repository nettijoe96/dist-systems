package utility;

public class ReplyData extends Packet{
    public Data data;

    public ReplyData( Data data ){
        super( "REPLY DATA", 0 );
        this.data = data;
    }

}
