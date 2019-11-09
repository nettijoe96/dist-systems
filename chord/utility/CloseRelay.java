package utility;

public class CloseRelay extends Packet {
 
    public int oldId; 
    public int successor; 

    public CloseRelay(int oldId, int successor) {
        super(Globals.CloseRelay, 0);
    }

}

