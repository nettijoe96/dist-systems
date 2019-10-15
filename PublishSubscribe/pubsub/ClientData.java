package pubsub;

import java.util.ArrayList;
import java.net.Socket;

class ClientData {

    public int id;
    public ArrayList<Topic> subscriptions;
    public ArrayList<Event> missedEvents;
    public String cachedIP;
    public int cachedPort;
    public Socket socket;    //we don't want a constant socket open, we this is fine for the first iteration of the protocol
    public Packet packet;

    ClientData(int id, Socket socket) {
        this.subscriptions = new ArrayList<Topic>();
        this.missedEvents = new ArrayList<Event>();
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
}
