package packet;

public class ClosePacket extends Packet{

    public String error; 
   
    public ClosePacket() {
        super("CLOSE");
    }

    public ClosePacket(String error) {
        super("CLOSE");
        this.error = error;
    }
}
