package pubsub;

public class Globals {

    public static final int BROKER_PORT = 60666;
//    static final String BROKER_IP = "172.10.0.2";      
    static final String BROKER_IP = "127.17.0.1";      
    static final int initDeviceId = 0;

    public static final int serverConnectionBacklog = 100;

        // Constants denoting certain types
    public static String ADVERTISE = "ADVERTISE";
    public static String CLOSE = "CLOSE";
    public static String CONNECT = "CONNECT";
    public static String PUBLISH = "PUBLISH";
    public static String NOTIFY = "NOTIFY";
    public static String SUBSCRIBE = "SUBSCRIBE";
    public static String UNSUBSCRIBE = "UNSUBSCRIBE";

    // CLI input 
    static final String EXIT_COMMAND = "EXIT";
    static final String LIST_TOPICS_COMMAND = "TOPICS";
    static final String HELP_COMMAND = "HELP";

    static final String BROKER_HELP = "The following commands are available:\n" +
        EXIT_COMMAND + "\n" +
        LIST_TOPICS_COMMAND + "\n" +
        HELP_COMMAND + "\n";
    
    static final String CLIENT_HELP = "The following commands are available:\n" +
        EXIT_COMMAND + "\n" +
        ADVERTISE + " [TOPIC NAME] [TOPIC DESCRIPTION] \n" +
        PUBLISH + " [TOPIC] [EVENT]\n" +
        SUBSCRIBE + " [TOPIC]\n" +
        UNSUBSCRIBE + " [TOPIC]\n" +
        HELP_COMMAND + "\n";



}
