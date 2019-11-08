package packet;

public class NewClient extends Packet {

    public String ip;

    public NewClient(int id, String ip) {
        super("NEW CLIENT", id);
        this.ip = ip;
    }

}


