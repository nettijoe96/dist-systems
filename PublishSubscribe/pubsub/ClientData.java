package pubsub;


import packet.Packet;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.Socket;
import java.util.concurrent.Semaphore;

class ClientData {

    public int id;
    public ArrayList<String> subscriptions;
    public Packet packet;
    public ArrayList<Topic> missedAds;
    public ArrayList<Event> missedEvents;
    public HashMap<String, Boolean> subscribeDict;
    private Semaphore clientMutex;

    ClientData(int id) {
        this.subscriptions = new ArrayList<String>();
        this.missedAds = new ArrayList<Topic>();
        this.missedEvents = new ArrayList<Event>();
        this.clientMutex = new Semaphore(1);
        this.subscribeDict = new HashMap<String, Boolean>();
        this.id = id;
    }

   
    /*
    checks if a notify has to be done because the missed messages are > 0
    
    @return bool
    */
    public boolean nonEmptyOutStream() {
        return this.missedAds.size() > 0 || this.missedEvents.size() > 0;  //at least one of the two queues are nonempty 
    }

    /*
    adds subscription to clients storage
    */
    public void addSubscription( Topic topic ){
        if( this.subscribeDict.containsKey(topic.topic) != true ){
            subscriptions.add( topic.topic );
            subscribeDict.put(topic.topic, true);
        }
    }

    /*
    removes subscription in clients storage
    */
    public void removeSubscription( Topic topic ){
        if( this.subscribeDict.get(topic.topic) == true ){
            subscriptions.remove( topic.topic );
            subscribeDict.put(topic.topic, false);
        }
    }


    /*
    trys to lock client and throws and exception if it cannot.
    */
    public void lockClient() throws InterruptedException {
        try {
            clientMutex.acquire();
        }
        catch (InterruptedException e) {
            throw e;
        }
    }

    /*
    unlocks client
    */
    public void unlockClient() {
        clientMutex.release();
    }

    /*
    checks if client is unlocked locked
    @param: boolean
    */
    public boolean checkAccess() {
        if(clientMutex.availablePermits() == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    /*
    checks if client is subscribed
    @param: boolean
    */
    public boolean isSubscribed(Topic topic) {
        return subscribeDict.get(topic.topic);
    }


    /*
    clears missed events
    */
    public void clearMissed() {
        this.missedEvents = new ArrayList<Event>();
        this.missedAds = new ArrayList<Topic>();
    }

  
   
    /*
    gets access when the client is free
    */
    public boolean waitTillAccess() {
        while (true) {
            try { 
                lockClient();
                return true;
            }
            catch (InterruptedException e) {
                continue;
            }
        } 
    }

}
