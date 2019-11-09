package utility;

public class CloseResponse extends Packet {
 
    public String successorIp;
    public int successor; 

    public CloseResponse(int successor, String successorIp) {
        super(Globals.CloseResponse, 0);
        this.successor = successor;
        this.successorIp = successorIp;
    }

}

