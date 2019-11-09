package utility;

public class Globals {

    public static final int PORT = 30000;
    public static final int ANCHOR_PORT = 20000;
    public static final String ANCHOR_IP = "172.10.0.2";      
    public static final int rows = 4;
    public static final int ringSize = 16;


    //packets
    //anchor
    public static final String AnchorConnect = "ANCHOR CONNECT";
    public static final String AnchorResponse = "ANCHOR RESPONSE";
    public static final String ClientClose = "CLIENT CLOSE";

    //node
    public static final String Connect = "CONNECT";
    public static final String Message = "message";
    public static final String NewClient = "NEW CLIENT";
    public static final String NewData = "NEW DATA";

    // CLI commands
    public static final String PRINT_TABLE = "print-table";
    public static final String NEW_KEY_VALUE = "add";
}
