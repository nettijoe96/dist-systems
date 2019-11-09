package utility;

public class Globals {

    public static final int PORT = 30000;
    public static final int ANCHOR_PORT = 20000;
    public static final String ANCHOR_IP = "172.10.0.2";      
    public static final int rows = 4;
    public static final int ringSize = 16;


    //packets
    public static final String ACK = "ACK";
    //anchor
    public static final String AnchorConnect = "ANCHOR CONNECT";
    public static final String AnchorResponse = "ANCHOR RESPONSE";
    public static final String CloseRelay = "CLOSE RELAY";
    public static final String CloseResponse = "CLOSE RESPONSE";

    //node
    public static final String InitiateClose = "INITIATE CLOSE";
    public static final String Connect = "CONNECT";
    public static final String Message = "message";
    public static final String NewClient = "NEW CLIENT";
    public static final String NewData = "NEW DATA";
    public static final String RequestData = "REQUEST DATA";
    public static final String ReplyData = "REPLY DATA";
    public static final String HashIdRelay = "HASH ID RELAY";

    // CLI commands
    public static final String PRINT_TABLE = "print-table";
    public static final String NEW_KEY_VALUE = "add";
    public static final String REQUEST_DATA = "request";
    public static final String CLOSE = "close";
    public static final String HASH_IDS = "ids";
}
