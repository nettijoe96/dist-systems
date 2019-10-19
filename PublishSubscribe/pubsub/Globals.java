package pubsub;

public class Globals {

    static final int BROKER_PORT = 60666;
//    static final String BROKER_IP = "172.10.0.2";      
    static final String BROKER_IP = "127.17.0.1";      
    static final int initDeviceId = 0;

    static final int serverConnectionBacklog = 100;

    // Constants denoting certain types (also used for cli commands)
    static final String ADVERTISE = "ADVERTISE";
    static final String CONNECT = "CONNECT";
    static final String PUBLISH = "PUBLISH";
    static final String NOTIFY = "NOTIFY";
    static final String SUBSCRIBE = "SUBSCRIBE";
    static final String UNSUBSCRIBE = "UNSUBSCRIBE";

    // CLI input 
    static final String EXIT_COMMAND = "EXIT";
    static final String LIST_TOPICS_COMMAND = "TOPICS";
    static final String HELP_COMMAND = "HELP";

    static final String BROKER_HELP = "The following commands are available:\n" +
        EXIT_COMMAND + "\n" +
        LIST_TOPICS_COMMAND + "\n" +
        HELP_COMMAND + "\n";


}
