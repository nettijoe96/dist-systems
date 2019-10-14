/*
 * A broker handles all communications from Advertisers and to Subscribers
 * It has a command line interface that...
 * It has threads that individually handle client connections
 */

package pubsub;


public class Broker{
	

    // How long to hold events
    static final int TIME_TO_HOLD = 10000;

	/*
	 * Start the repo service
	 */
	private void startService() {
        // Start a service that handles the connecting of new nodes to the network
            Globals globals = new Globals();
            BrokerListener listener = new BrokerListener( globals.SERVER_PORT );
            Thread listenerThread = new Thread( listener );
            listenerThread.start();
            System.out.println("Broker Listener Started");

        // Start a thread that will listen for CLI input
            BrokerCLI cli = new BrokerCLI();
            Thread cliThread = new Thread( cli );
            cliThread.start();
            System.out.println("Broker CLI Started");
		
	}

	/*
	 * notify all subscribers of new event 
	 */
	private void notifySubscribers() {
		
	}
	
	/*
	 * add new topic when received advertisement of new topic
	 */
	private void addTopic(){
		
	}
	
	/*
	 * add subscriber to the internal list
	 */
	private void addSubscriber(){
		
	}
	
	/*
	 * remove subscriber from the list
	 */
	private void removeSubscriber(){
		
	}
	
	/*
	 * show the list of subscriber for a specified topic
	 */
	private void showSubscribers(){
		
	}
	
	
	public static void main(String[] args) {
            Broker broker = new Broker();
            broker.startService();
	}
}
