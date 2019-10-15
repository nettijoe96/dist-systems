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
