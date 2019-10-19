package pubsub;

public class Globals {

    public static final int BROKER_PORT = 60666;
//    static final String BROKER_IP = "172.10.0.2";      
    public static final String BROKER_IP = "127.0.0.1";      
    public static final int initDeviceId = 0;

    public static final int serverConnectionBacklog = 100;

    // Constants denoting certain types
    public static String ADVERTISE = "ADVERTISE";
    public static String CLOSE = "CLOSE";
    public static String CONNECT = "CONNECT";
    public static String PUBLISH = "PUBLISH";
    public static String NOTIFY = "NOTIFY";
    public static String SUBSCRIBE = "SUBSCRIBE";
    public static String UNSUBSCRIBE = "UNSUBSCRIBE";

    // CLI input to exit
    public static final String EXITLINE = "exit()";


}
