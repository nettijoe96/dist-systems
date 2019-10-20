/*
 * A broker handles all communications from Advertisers and to Subscribers
 * It has a command line interface that...
 * It has threads that individually handle client connections
 */

package pubsub;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;
import java.net.Socket;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class Broker{
	

    // How long to hold events
    static final int TIME_TO_HOLD = 10000;

    public ArrayList<Topic> topics = new ArrayList<>();
    public Semaphore topicsMutex = new Semaphore(1);
    public ArrayList<Event> events = new ArrayList<>();
    public Semaphore eventsMutex = new Semaphore(1);
    public ArrayList<ClientData> clients = new ArrayList<>();
    public HashMap<String, ArrayList<Event>> topicEvents = new HashMap<String, ArrayList<Event>>();
    public HashMap<String, ArrayList<ClientData>> subscriptions = new HashMap<String, ArrayList<ClientData>>();
    public HashMap<Integer, ClientData> clientMap = new HashMap<Integer, ClientData>();
    private int nextid;
    private Globals globals;

    
    public Broker() {
        this.globals = new Globals();
        this.nextid = this.globals.initDeviceId + 1;
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

            // Start a thread that will listen for CLI input  //TODO
            BrokerCLI cli = new BrokerCLI(this);
            Thread cliThread = new Thread(cli);
            cliThread.start();
            System.out.println("Broker CLI Started");
        } catch( UnknownHostException e ){
            throw e;
        }
        catch( IOException e ){
            throw e;
        }
    }

    /*
     * Exit the service
     */
    protected void exitService(){
        // This should be handled better but it will work for now?
        System.out.println("Broker service stopping");
        System.exit(0);
    }

    /*
    getClient

    gets client from clientMap
    @param id
    @return clientData
    */
    public ClientData getClient(int id) throws IllegalArgumentException {
    
        ClientData client = clientMap.get((Integer) id);
        if(client == null) {
            throw new IllegalArgumentException("invalid client id");
        }
        return client;

    }

   
    /*
    addClient

    adds new client to clientMap and clients ArrayList
    @param client clientData
    */
    public void addClient(ClientData client) {
 
        clientMap.put((Integer)client.id, client); 
        clients.add(client);
         
        //fill missed ads in new client
        for(int i = 0; i < topics.size(); i++) {
            client.missedAds.add(topics.get(i));
        }
    } 

    /*
    getNewId

    get new id for a new client
    */
    public int getNewId() {
        ReentrantLock lock = new ReentrantLock();
        lock.lock(); //we need to lock so that each client has a different id (stop race condition)
        int id = this.nextid;
        this.nextid++;
        lock.unlock();
        return id; 
    }


    /*
    addEvent

    adds new topic to topics ArrayList
    @param topic
    */
    public void addEvent(Event event, ClientData currClient) {
        Topic topic = event.topic;
        if (topicExists(topic)) {
            System.out.println("addEvent");
            this.topicEvents.get(topic.topic).add(event);  //add event to list of events
            ArrayList<ClientData> subscribers = this.subscriptions.get(topic.topic);
            for(int i = 0; i < subscribers.size(); i++) {
                ClientData client = subscribers.get(i);
                System.out.print("subscribers: ");
                System.out.println(client.id);
                if(client.id != currClient.id) {
                    client.waitTillAccess();   //TODO: this is a naive way of doing it because one problem thing can being it to a standstill
                    client.missedEvents.add(event);
                    client.unlockClient();
                }
                else {
                    client.missedEvents.add(event);
                }
            }
        }
         
    } 

    /*
    topicExists
   
    check if a topic exists

    */
    public boolean topicExists(Topic topic) {
        for(int i = 0; i < this.topics.size(); i++) {
            if (topic.topic.equals(this.topics.get(i).topic)) {
                return true;
            }
        }
        return false;
    }


    /*
    addTopic

    adds new topic to topics ArrayList
    @param topic
    */
    public void addTopic(Topic topic, ClientData currClient) {
        if (!topicExists(topic)) {  //only add topic if it doesn't already exist
            topics.add(topic);
            System.out.println("in addTopic");            
            System.out.println(topics);            
            subscriptions.put(topic.topic, new ArrayList<ClientData>());
            topicEvents.put(topic.topic, new ArrayList<Event>());
            for(int i = 0; i < clients.size(); i++) {
                ClientData client = clients.get(i);
                if(client.id != currClient.id) {
                    client.waitTillAccess();   //TODO: this is a naive way of doing it because one problem thing can bring it to a standstill
                    client.missedAds.add(topic);
                    client.unlockClient();
                }
                else {
                    client.missedAds.add(topic);
                }
            }
            System.out.println("in addTopic");            
            System.out.println(topics);            
        }
    } 

    /*
    subscribe
    
    */
    public void subscribe(Topic topic, ClientData client) {
        if(topicExists(topic)) {
            ArrayList<ClientData> subscribedClients = subscriptions.get(topic.topic);
            subscribedClients.add(client);
            for(Event e : topicEvents.get(topic.topic)) {
                client.missedEvents.add(e);
            }
        }
    }


    /*
    * unsubscribe

    */
    public void unsubscribe(Topic topic, ClientData client) {
        if(topicExists(topic)) {
            ArrayList<ClientData> subscribedClients = subscriptions.get(topic.topic);
            for(int i = 0; i < subscribedClients.size(); i++) {
                ClientData c = subscribedClients.get(i);
                if(c.id == client.id) {
                    subscribedClients.remove(i);
                }
            }
        }
    }

    public Topic getTopicByName( String topicName ){
        try{
            this.topicsMutex.acquire();
            for( Topic topic : this.topics ){
                System.out.print("224\n");
                System.out.println(topicName);
                System.out.println(topic.topic);
                if( topic.topic.equals(topicName) ) {
                    this.topicsMutex.release();
                    return topic;
                }
            }
            this.topicsMutex.release();
        }catch( InterruptedException e ){
            System.out.println("Interupted Exception");
            e.printStackTrace();
        }

        return null;
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
