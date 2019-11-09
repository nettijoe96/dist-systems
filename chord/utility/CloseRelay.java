package utility;

public class CloseRelay extends Packet {
 
    public int oldId; 
    public int successor; 
    public String successorIp;

    public CloseRelay(int oldId, int successor, String successorIp) {
        super(Globals.CloseRelay, 0);
        this.oldId = oldId;
        this.successor = successor;
        this.successorIp = successorIp;
    }

}

