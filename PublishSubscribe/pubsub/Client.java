

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
       System.out.println("Initialized client with UUID " + this.id );
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

    
    /*
    processNotify
    processes a notify packet. This is called at the end of callmanager when a notify packet is received  
    @param notifyPacket
    */
    private void processNotify(NotifyPacket packet) {
        for(int i = 0; i < packet.events.size(); i++) {
            Event event = packet.events.get(i);
            Topic topic = event.topic;
            
            if( topicEvents.containsKey( topic.topic ) ){
                this.topicEvents.get(topic.topic).add(event);
            }else{
                this.topicEvents.put( topic.topic, new ArrayList<Event>() );
                this.topicEvents.get( topic.topic ).add( event );
            }
        }
        for(int i = 0; i < packet.ads.size(); i++) {
            Topic topic = packet.ads.get(i);
            this.ads.add(topic.topic);
            this.nameTopic.put(topic.topic, topic);
        }
    }

    /*
    subscribes to a topic if it exists
    @param topic name
    */
    private void subscribe(String name) {
        if(topicExists(name)) {
            Topic topic = getTopicByName(name);
            subscriptions.add(name);
            topicEvents.put(name, new ArrayList<Event>()); 
        }        
        else {
            System.out.println(nameTopic);
        }
    }


    /*
    unsubscribes to a topic
    @param topic name
    */
    private void unsubscribe(String name) {
        subscriptions.remove(name);
    }


    /*
    callManager
    
    a general function for processing all communications with manager/broker, 
    whether those communications get initiated in heartbeat or in clientcli

    at the end we process notify messages if the exist
   
    @param: calltype string
    @param: general input
    */
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
                System.out.println("Your UUID is " + this.id );
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
                Topic topic = (Topic) input;
                unsubscribe(topic.topic);
                UnsubscribePacket unsubPacket = new UnsubscribePacket(topic, this.id);       
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


         
        /*
        every 5 seconds we try to acquire access to client and send a connect heartbeat messsage 
        in the hope of getting a notify back in response from the server/broker
        */
        public void run() {
           
            while(true) {
                try {
                    sleep(10000); //5 seconds
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
   


    /*
    finds topic

    @param: name
    @return: topic
    */
    public Topic getTopicByName( String topicName ){
        return nameTopic.get(topicName);
    }

    /*
    finds if topic exists

    @param: topic name
    @return: boolean
    */
    public boolean topicExists(String topicName) {
        return nameTopic.get(topicName) != null; 
    }


    /*
    locks client, otherwise throws an error
    */
    public void lockClient() throws InterruptedException {
        try {
            socketMutex.acquire();
        }
        catch (InterruptedException e) {
            throw e;
        }
    }

    /*
    unlocks client
    */
    public void unlockClient() {
        socketMutex.release();
    }


    /*
    checks if client is unlocked locked
    @param: boolean
    */
    public boolean checkAccess() {
        if(socketMutex.availablePermits() == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    /*
    gets access when the client is free
    */
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

    /*
    main client function. 

    sends an initial connect
    starts cli
    start heartbeat
    */
    public static void main(String[] args) {
        Globals globals = new Globals();     
        if( args.length == 1){
            Client client = new Client( Integer.parseInt( args[0] ) );
            client.callManager(globals.CONNECT, "");
            client.startCLI();
            client.startHeartbeat();
        }else{
            Client client = new Client();
            client.callManager(globals.INITIALCONNECT, "");        
            client.startCLI();
            client.startHeartbeat();
        }
    }
}


