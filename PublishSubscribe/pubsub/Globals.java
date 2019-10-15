package pubsub;

public class Globals {

    static final int BROKER_PORT = 60666;
//    static final String BROKER_IP = "172.10.0.2";      
    static final String BROKER_IP = "127.0.0.1";      
    static final int initDeviceId = 0;

    static final int serverConnectionBacklog = 100;

    // Constants denoting certain types
    static final String CONNECT = "CONNECT";
    static final String PUBLISH = "PUBLISH";
    static final String SUBSCRIBE = "SUBSCRIBE";
    static final String UNSUBSCRIBE = "UNSUBSCRIBE";


}
