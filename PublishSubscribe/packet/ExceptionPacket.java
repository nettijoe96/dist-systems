package packet;



public class ExceptionPacket extends Packet {

    public String message; 
    public static final String EXCEPTION = "EXCEPTION";


    public ExceptionPacket(String message) {
        super(EXCEPTION);     
        this.message = message;

    }
}
