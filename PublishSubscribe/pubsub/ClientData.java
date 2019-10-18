package pubsub;

import java.util.ArrayList;
import java.util.HashMap;
import java.net.Socket;
import java.util.concurrent.Semaphore;

class ClientData {

    public int id;
    public ArrayList<Topic> subscriptions;
    public String cachedIP;
    public int cachedPort;
    public Socket socket;    //we don't want a constant socket open, we this is fine for the first iteration of the protocol
    public Packet packet;
    public ArrayList<Topic> missedAds;
    public ArrayList<Event> missedEvents;
    public HashMap<Topic, Boolean> subscribeDict;
    private Semaphore clientMutex;
    //TODO: add dictionary for easily keeping track of subscriptions/unsubscriptions topics

    ClientData(int id, Socket socket) {
        this.subscriptions = new ArrayList<Topic>();
        this.missedAds = new ArrayList<Topic>();
        this.missedEvents = new ArrayList<Event>();
        this.clientMutex = new Semaphore(1);
        this.subscribeDict = new HashMap<Topic, Boolean>();
        this.id = id;
        updateClientWithNewSocket(socket); 
    }

    public void updateClientWithNewSocket(Socket socket) {
        this.cachedIP = socket.getInetAddress().toString();
        this.cachedPort = socket.getPort(); 
        this.socket = socket;
    } 

   
    public void setPacketToSend(Packet packet) {
        this.packet = packet;
    }

   
    public Packet getPackToSend(Packet packet) {
        return packet; 
    }

    public boolean nonEmptyOutStream() {
        return this.missedAds.size() > 0 || this.missedEvents.size() > 0;  //at least one of the two queues are nonempty 
    }

    public void addSubscription( Topic topic ){
        if( this.subscribeDict.get(topic) != true ){
            subscriptions.add( topic );
            subscribeDict.put(topic, true);
        }
    }

    public void removeSubscription( Topic topic ){
        if( this.subscribeDict.get(topic) == true ){
            subscriptions.remove( topic );
            subscribeDict.put(topic, false);
        }
    }


    public void lockClient() throws InterruptedException {
        try {
            clientMutex.acquire();
        }
        catch (InterruptedException e) {
            throw e;
        }
    }

    public void unlockClient() {
        clientMutex.release();
    }


    public boolean checkAccess() {

        if(clientMutex.availablePermits() == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isSubscribed(Topic topic) {
        return subscribeDict.get(topic);
    }

}