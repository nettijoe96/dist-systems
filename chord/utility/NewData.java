package utility;

public class NewData extends Packet {

    public Data data;

    public NewData(int id, Data data) {
        super("NEW DATA", id);
        this.data = data;
    }

}


