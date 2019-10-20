package pubsub;

public class Globals {

    public static final int BROKER_PORT = 60666;
//    static final String BROKER_IP = "172.10.0.2";      
    public static final String BROKER_IP = "127.17.0.1";      
    public static final int initDeviceId = 0;

    public static final int serverConnectionBacklog = 100;

        // Constants denoting certain types
    public static String ADVERTISE = "ADVERTISE";
    public static String CLOSE = "CLOSE";
    public static String CONNECT = "CONNECT";
    public static String INITIALCONNECT = "INITIALCONNECT";
    public static String PUBLISH = "PUBLISH";
    public static String NOTIFY = "NOTIFY";
    public static String SUBSCRIBE = "SUBSCRIBE";
    public static String UNSUBSCRIBE = "UNSUBSCRIBE";

    // CLI input 
    static final String EXIT_COMMAND = "Q";
    static final String LIST_TOPICS = "TOPICS";
    static final String HELP_COMMAND = "HELP";
    static final String LIST_SUBSCRIPTIONS = "SUBSCRIPTIONS";
    static final String TOPIC = "TOPIC";
    static final String SUBSCRIPTION = "SUBSCRIPTION";
    static final String EVENT = "EVENT";

    static final String BROKER_HELP = "The following commands are available:\n" +
        EXIT_COMMAND + "\n" +
        LIST_TOPICS + "\n" +
        "event [topic] [event name]  \n" +  
        "subscription [topic] \n" +  
        HELP_COMMAND + "\n";
    
    static final String CLIENT_HELP = "The following commands are available:\n" +
        "advertise [topic name] [topic description] \n" +
        "publish [topic] [event name] [event payload]\n" +
        "subscribe [topic]\n" +
        "unsubscribe [topic]\n" +

        "\n show information commands: \n" + 
        "subscriptions \n" +  
        "subscription [topic] \n" +  
        "topics \n" +  
        "event [topic] [event name]  \n" +  

        
        "\n help \n" + 
        "q \n";



}
