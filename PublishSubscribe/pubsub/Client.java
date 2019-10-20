

/*
* Client main class. The client can tell broker to publish x or subscribe to y. 
*/


package pubsub;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.lang.ClassNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import packet.*;

public class Client {

    private int id; 
    private Globals globals;
    public ArrayList<String> ads;  
    public ArrayList<String> subscriptions;
    public HashMap<String, ArrayList<Event>> topicEvents;
    public HashMap<String, Topic> nameTopic;
    private Semaphore socketMutex;

    public Client() {
       this.id = globals.initDeviceId;
       init();
    }

    public Client(int id) {
       this.id = id;
       init();
    }


    private void init() {
       this.globals = new Globals();        
       this.ads = new ArrayList<String>();
       this.subscriptions = new ArrayList<String>();
       this.topicEvents = new HashMap<String, ArrayList<Event>>();
       this.nameTopic = new HashMap<String, Topic>();
       this.socketMutex = new Semaphore(1);
    }

    private void processNotify(NotifyPacket packet) {
        System.out.print("in process notify");
        for(int i = 0; i < packet.events.size(); i++) {
            Event event = packet.events.get(i);
            Topic topic = event.topic;
            this.topicEvents.get(topic.topic).add(event);
        }
        for(int i = 0; i < packet.ads.size(); i++) {
            Topic topic = packet.ads.get(i);
            this.ads.add(topic.topic);
            this.nameTopic.put(topic.topic, topic);
        }
    }

   
    private void subscribe(String name) {
        if(topicExists(name)) {
            Topic topic = getTopicByName(name);
            subscriptions.add(name);
            topicEvents.put(name, new ArrayList<Event>()); 
        }        
        else {
            System.out.println("doesn't exist in subscribe"); 
            System.out.println(nameTopic);
        }
    }

    public <InputType> void callManager(String callType, InputType input) {
    
        Globals globals = new Globals();
        try {
            Socket socket = new Socket(this.globals.BROKER_IP, this.globals.BROKER_PORT);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            if (callType.equals(globals.ADVERTISE)) {
                AdvertisePacket adPacket = new AdvertisePacket((Topic) input, this.id);       
                out.writeObject(adPacket);
            }
            else if (callType.equals(globals.CONNECT)) {
                ConnectPacket connpacket = new ConnectPacket(this.globals.CONNECT, this.id);       
                out.writeObject(connpacket);
                ConnackPacket connack = (ConnackPacket) in.readObject();
            }
            else if (callType.equals(globals.INITIALCONNECT)) {
                ConnectPacket connpacket = new ConnectPacket(this.globals.CONNECT, this.globals.initDeviceId);       
                out.writeObject(connpacket);
                System.out.println("Establishing connection");
                ConnackPacket connack = (ConnackPacket) in.readObject();
                this.id = connack.clientId;
                System.out.println("Recieved connack, connection established");
            }
            else if (callType.equals(globals.PUBLISH)) {
                PublishPacket pubPacket = new PublishPacket((Event) input, this.id);       
                out.writeObject(pubPacket);
            }
            else if (callType.equals(globals.SUBSCRIBE)) {
                Topic topic = (Topic) input;
                subscribe(topic.topic);
                SubscribePacket subPacket = new SubscribePacket(topic, this.id);       
                out.writeObject(subPacket);
            }
            else if (callType.equals(globals.UNSUBSCRIBE)) {
                UnsubscribePacket unsubPacket = new UnsubscribePacket((Topic) input, this.id);       
                out.writeObject(unsubPacket);
            }
            else {
                throw new IllegalArgumentException("callType is abnormal in callmanager:" + callType);
            }
                        
 
            Packet packet = (Packet) in.readObject();  //either a close packet or a notify packet
            if (packet instanceof NotifyPacket) {
                processNotify((NotifyPacket) packet);
                packet = (Packet) in.readObject();     //close packet
            }

            if (packet instanceof ClosePacket) { 
                return;   //TODO: let other side socket.close()? this seems gross. I just don't want to return 2 things from advertise, connect, etc
            }
            else {
                throw new IOException("callmanager didn't recieve closepacket to finish session");
            }
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    /*
    * creates a new thread for heartbeat
    */
    public void startHeartbeat() {
        Heartbeat hb = new Heartbeat(this);
        hb.start(); 
    }

    private class Heartbeat extends Thread {
       
        Globals globals; 
        Client client;
  
        Heartbeat(Client client) {
            this.globals = new Globals();
            this.client = client;
        }


        public void run() {
           
            while(true) {
                try {
                    sleep(5000); //30 seconds
                    if(client.checkAccess()) { 
                        client.waitTillAccess(); 
                        client.callManager(globals.CONNECT, "");        
                        client.unlockClient();
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
     
            }
 
        } 
        
    }


    /*
    * creates a new thread for listening to stdin. 
    */
    public void startCLI() {
        ClientCLI cli = new ClientCLI( this );
        Thread cliThread = new Thread( cli );
        cliThread.start(); 
    }
   

    public void exitService(){
        System.out.println("This is not implemented yet...");
    }


    public static void main(String[] args) {
        Client client = new Client();     //TODO: allow for cmd args or file processing to determine if client already has a deviceId from being run before. If so, use Client(deviceUUId) constructor
        Globals globals = new Globals();     
        client.callManager(globals.INITIALCONNECT, "");        
        client.startCLI();
        client.startHeartbeat();
    }


    public Topic getTopicByName( String topicName ){
        return nameTopic.get(topicName);
    }

    public boolean topicExists(String topicName) {
        System.out.println("in topic exists " + topicName);
        System.out.println(nameTopic.get(topicName));
        return nameTopic.get(topicName) != null; 
    }

    public void lockClient() throws InterruptedException {
        try {
            socketMutex.acquire();
        }
        catch (InterruptedException e) {
            throw e;
        }
    }

    public void unlockClient() {
        socketMutex.release();
    }

    public boolean checkAccess() {
        if(socketMutex.availablePermits() == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean waitTillAccess() {
        while (true) {    //TODO: should have a timeout
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


