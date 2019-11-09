package utility;

public class HashIdRelay extends Packet {

    public ArrayList<Integer> hashIds;

    public HashIdRelay(int id, ArrayList<Integer> hashIds) {
        super(Globals.HashIdRelay, id);
        this.hashIds = hashIds;
    }

}


