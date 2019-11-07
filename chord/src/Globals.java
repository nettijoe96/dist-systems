package src;

public class Globals {

    public static final int PORT = 30000;
    public static final int ANCHOR_PORT = 20000;
    public static final String ANCHOR_IP = "172.10.0.2";      
    public static final int RING_SPACE_SIZE = 16;
    public static final String CONNECT = "connect";
    public static final String DELIMITER = ";";
    public static final int rows = 4;
    public static final int ringSize = 16;


    //packets
    //anchor
    public static final String AnchorConnect = "ANCHOR CONNECT";
    public static final String AnchorResponse = "ANCHOR RESPONSE";

    //connect
    public static final String Connect = "CONNECT";

    // CLI commands
    public static final String PRINT_TABLE = "print-table";
}
