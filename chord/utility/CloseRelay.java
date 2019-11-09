package utility;

public class CloseRelay extends Packet {
 
    public int oldId; 
    public int successor; 

    public CloseRelay(int oldId, int successor) {
        super(Globals.CloseRelay, 0);
        this.oldId = oldId;
        this.successor = successor;
    }

}

