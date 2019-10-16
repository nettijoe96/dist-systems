package pubsub;

import java.util.ArrayList;
import java.util.Queue;
import java.net.Socket;
import java.util.concurrent.Semaphore;

class ClientData {

    public int id;
    public ArrayList<Topic> subscriptions;
    public ArrayList<Event> missedEvents;
    public String cachedIP;
    public int cachedPort;
    public Socket socket;    //we don't want a constant socket open, we this is fine for the first iteration of the protocol
    public Packet packet;
    public Queue<Topic> topicAdvertiseQueue;
    public Queue<Event> eventQueue;
    private Semaphore clientMutex;

    ClientData(int id, Socket socket) {
        this.subscriptions = new ArrayList<Topic>();
        this.missedEvents = new ArrayList<Event>();
        this.clientMutex = new Semaphore(1);
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


    public void addSubscription( Topic topic ){
        if( !subscriptions.contains( topic ) ){
            subscriptions.add( topic );
        }
    }

    public void lockClient() {
        clientMutex.acquire();
    }

    public void unlockClient() {
        clientMutex.release();
    }


    public boolean checkAccess() {

        if(clientMutex.availablePermits() == 1) {
            return true
        }
        else {
            return false
        }
    }

}
