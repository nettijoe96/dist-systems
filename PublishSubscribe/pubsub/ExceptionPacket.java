package pubsub;



class ExceptionPacket extends Packet {

    public String message; 
    public static final String EXCEPTION = "EXCEPTION";


    ExceptionPacket(String message) {
        super(EXCEPTION);     
        this.message = message;

    }
}
