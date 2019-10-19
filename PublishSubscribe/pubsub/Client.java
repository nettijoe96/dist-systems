

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

public class Client {


    private int id; 
    private Globals globals;
    private ArrayList<Topic> ads;  
    private ArrayList<Topic> subscriptions;
    private HashMap<Topic, ArrayList<Event>> topicEvents;

    public Client() {
       this.globals = new Globals();        
       this.id = globals.initDeviceId;
       this.ads = new ArrayList<Topic>();
       this.subscriptions = new ArrayList<Topic>();
       this.topicEvents = new HashMap<Topic, ArrayList<Event>>();
    }

    public Client(int id) {
       this.globals = new Globals();        
       this.id = id;
       this.ads = new ArrayList<Topic>();
       this.topicEvents = new HashMap<Topic, ArrayList<Event>>();
    }


    public void initialConnect() throws UnknownHostException, IOException, ClassNotFoundException {

       //connect to server
       try {
           Socket socket = new Socket(this.globals.BROKER_IP, this.globals.BROKER_PORT);
           ConnectPacket connpacket = new ConnectPacket(this.globals.CONNECT, this.globals.initDeviceId);       
           ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
           ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
           out.writeObject(connpacket);
           ConnackPacket connack = (ConnackPacket) in.readObject();
           this.id = connack.clientId;
           System.out.println("recieved connack");
       }
       catch (UnknownHostException e) {
           throw e; 
       }
       catch (IOException e) {
           throw e; 
       }
       catch (ClassNotFoundException e) {
           throw e; 
       }

    }

    public ObjectInputStream connect() throws UnknownHostException, IOException, ClassNotFoundException {

       //connect to server
       try {
           Socket socket = new Socket(this.globals.BROKER_IP, this.globals.BROKER_PORT);
           ConnectPacket connpacket = new ConnectPacket(this.globals.CONNECT, this.id);       
           ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
           ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
           out.writeObject(connpacket);
           return in;
       }
       catch (UnknownHostException e) {
           throw e; 
       }
       catch (IOException e) {
           throw e; 
       }
    }

    public ObjectInputStream advertise(Topic topic) throws UnknownHostException, IOException, ClassNotFoundException {

       //connect to server
       try {
           Socket socket = new Socket(this.globals.BROKER_IP, this.globals.BROKER_PORT);
           AdvertisePacket packet = new AdvertisePacket(topic, this.id);       
           ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
           ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
           out.writeObject(packet);
           //ConnackPacket connack = (ConnackPacket) in.readObject(); //change if we want to get anything back
           return in;
       }
       catch (UnknownHostException e) {
           throw e; 
       }
       catch (IOException e) {
           throw e; 
       }
    }


/*
    public Socket publish(Event event) {
        Socket socket = new Socket(this.globals.BROKER_IP, this.globals.BROKER_PORT);
        return socket;  
    }
*/

    private void processNotify(NotifyPacket packet) {
        System.out.print("in process notify");
        for(int i = 0; i < packet.events.size(); i++) {
            Event event = packet.events.get(i);
            Topic topic = event.topic;
            this.topicEvents.get(topic).add(event);
        }
        for(int i = 0; i < packet.ads.size(); i++) {
            Topic topic = packet.ads.get(i);
            this.ads.add(topic);
        }
    }

    public <InputType> void callManager(String callType, InputType input) {
    
        Globals globals = new Globals();
        ObjectInputStream in;
        try {
            if (callType.equals(globals.ADVERTISE)) {
                in = advertise((Topic) input);
            }
            else if (callType.equals(globals.CONNECT)) {
                in = connect();
            }
            else if (callType.equals(globals.PUBLISH)) {
                in = connect();  //TODO
            }
            else if (callType.equals(globals.SUBSCRIBE)) {
                in = connect();  //TODO
            }
            else if (callType.equals(globals.UNSUBSCRIBE)) {
                in = connect();  //TODO
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



    private class Heartbeat extends Thread {
    
        public void run() {
            
        } 
        

    }



    /*
    * creates a new thread for listening to stdin. 
    */
    public void startCLI() {
        ClientCli cli = new ClientCli();
        cli.start(); 
    }
    

    private class ClientCli extends Thread {
    
        public void run() {

            //listen on stdin

            //create appropiate packet for the command called

            //create connect object and pass packet in

            //


        } 

    }


    public static void main(String[] args) {
        Client client = new Client();     //TODO: allow for cmd args or file processing to determine if client already has a deviceId from being run before. If so, use Client(deviceUUId) constructor
        Globals globals = new Globals();     

        try {
            client.initialConnect();        
//        client.startCLI();  
//        client.listen();    //listens for server to connect to it (server keeps cache of ip addresses)

            //test
            Topic topic = new Topic("1_topic_1", "test");
            client.callManager(globals.ADVERTISE, topic);
            System.out.println(client.ads);
        }
        catch (UnknownHostException e) {
            System.out.println(e); 
        }
        catch (IOException e) {
            System.out.println(e); 
        }
        catch (ClassNotFoundException e) {
            System.out.println(e); 
        }
    }


}


