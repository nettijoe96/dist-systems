/*
 * The BrokerCLI listens to stdin and is used to monitor the broker
 */


package pubsub;

public class BrokerCLI implements Runnable{

    Broker broker;  

    public BrokerCLI(Broker broker) {
        this.broker = broker; 
    }

    public void run(){
    }
}
