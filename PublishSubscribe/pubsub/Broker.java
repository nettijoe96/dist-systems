/*
 * A broker handles all communications from Advertisers and to Subscribers
 * It has a command line interface that...
 * It has threads that individually handle client connections
 */

package pubsub;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;
import java.net.*;
import java.util.*;
import java.io.IOException;
import java.net.UnknownHostException;


public class Broker{
	

    // How long to hold events
    static final int TIME_TO_HOLD = 10000;
    public SubList subList;
    public PubList pubList;
    public ArrayList<Topic> topics = new ArrayList<>();
    public Semaphore topicsMutex = new Semaphore(1);
    public ArrayList<Event> events = new ArrayList<>();
    public Semaphore eventsMutex = new Semaphore(1);
    private int nextUUID;
    private Globals globals;

    
    public Broker() {
        this.subList = new SubList();
        this.pubList = new PubList();
        this.globals = new Globals();
        this.nextUUID = this.globals.initDeviceUUID;
    } 

    /*
    * Start the repo service
    */
    private void startService() throws UnknownHostException, IOException {
        // Start a service that handles the connecting of new nodes to the network
        try {
            Globals globals = new Globals();
            BrokerListener listener = new BrokerListener(this);
            Thread listenerThread = new Thread( listener );
            listenerThread.start();
            System.out.println("Broker Listener Started");

            // Start a thread that will listen for CLI input
            BrokerCLI cli = new BrokerCLI(this);
            Thread cliThread = new Thread( cli);
            cliThread.start();
            System.out.println("Broker CLI Started");
        } catch( UnknownHostException e ){
            throw e;
        }
        catch( IOException e ){
            throw e;
        }


    }


    public int getNewUUID() {
        ReentrantLock lock = new ReentrantLock();
        lock.lock(); //we need to lock so that each client has a different UUID (stop race condition)
        int UUID = this.nextUUID;
        this.nextUUID++;
        lock.unlock();
        return UUID; 
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
        try {
            Broker broker = new Broker();
            broker.startService();
        } catch( UnknownHostException e ){
            e.printStackTrace();
        }
        catch( IOException e ){
            e.printStackTrace();
        }
    }

}
