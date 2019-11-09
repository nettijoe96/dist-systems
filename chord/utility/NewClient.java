package utility;

public class NewClient extends Packet {

    public String ip;

    public NewClient(int id, String ip) {
        super(Globals.NewClient, id);
        this.ip = ip;
    }

}


