package pubsub;


import packet.Packet;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.Socket;
import java.io.IOException;
import java.net.UnknownHostException;
import java.net.ConnectException;
import java.util.concurrent.Semaphore;
import packet.NotifyPacket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class ClientData {

    public int id;
    public ArrayList<String> subscriptions;
    public Packet packet;
    public ArrayList<Topic> missedAds;
    public ArrayList<Event> missedEvents;
    public HashMap<String, Boolean> subscribeDict;
    private Semaphore clientMutex;
    public String cachedIP;
    public int cachedPort; 
    private Globals globals;

    ClientData(int id) {
        this.subscriptions = new ArrayList<String>();
        this.missedAds = new ArrayList<Topic>();
        this.missedEvents = new ArrayList<Event>();
        this.clientMutex = new Semaphore(1);
        this.subscribeDict = new HashMap<String, Boolean>();
        this.id = id;
        this.globals = new Globals();
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
    checks if client is subscribed
    @param: boolean
    */
    public boolean isSubscribed(Topic topic) {
        return subscribeDict.get(topic.topic);
    }



    private int getClientPort() {
        return globals.startingPort + id;
    } 

    /*
    clears missed events
    */
    public void clearMissed() {
        this.missedEvents = new ArrayList<Event>();
        this.missedAds = new ArrayList<Topic>();
    }

  
    public void notifyTopic(Topic topic) {
        ArrayList<Topic> ads = new ArrayList<Topic>();
        ads.add(topic);
        NotifyPacket packet = new NotifyPacket(new ArrayList<Event>(), ads);
        Notify notifyThread = new Notify(packet);
        notifyThread.start();
    }

    public void notifyEvent(Event event) {
        ArrayList<Event> events = new ArrayList<Event>();
        events.add(event);
        NotifyPacket packet = new NotifyPacket(events, new ArrayList<Topic>());
        Notify notifyThread = new Notify(packet);
        notifyThread.start();
    }


    private class Notify extends Thread {

        NotifyPacket packet;

        Notify(NotifyPacket packet){
            this.packet = packet;
        }

        public void run() {
            try {
                lockClient();
                System.out.println(cachedIP);
                System.out.println(getClientPort());
                Socket socket = new Socket(cachedIP, getClientPort());   
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(this.packet);
                socket.close();
                unlockClient();
            }
            catch(ConnectException e) {
                for(int i = 0; i < packet.events.size(); i++) {
                    Event event = packet.events.get(i);
                    missedEvents.add(event);
                }
                for(int i = 0; i < packet.ads.size(); i++) {
                    Topic topic = packet.ads.get(i);
                    missedAds.add(topic);
                }
                
            }
            catch (UnknownHostException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }

}
