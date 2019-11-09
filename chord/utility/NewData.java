package utility;

public class NewData extends Packet {

    public Data data;

    public NewData(int id, Data data) {
        super(Globals.NewData, id);
        this.data = data;
    }

}


