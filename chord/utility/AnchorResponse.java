package utility;

import java.util.HashMap;

public class AnchorResponse extends Packet {

    public HashMap<Integer, String> nodeTable;

    public AnchorResponse(HashMap<Integer, String> nodeTable) {
        super("ANCHOR RESPONSE", 0);
        this.nodeTable = nodeTable;
    }

}
