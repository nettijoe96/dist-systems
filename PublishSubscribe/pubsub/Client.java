

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

public class Client {


    private int id; 
    private Globals globals;
  

    public Client() {
       this.globals = new Globals();        
       this.id = globals.initDeviceId;
    }


    public Client(int id) {
       this.globals = new Globals();        
       this.id = id;
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

    public void advertise(Topic topic) throws UnknownHostException, IOException, ClassNotFoundException {

       //connect to server
       try {
           Socket socket = new Socket(this.globals.BROKER_IP, this.globals.BROKER_PORT);
           AdvertisePacket packet = new AdvertisePacket(topic, this.id);       
           ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
           ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
           out.writeObject(packet);
           //ConnackPacket connack = (ConnackPacket) in.readObject(); //change if we want to get anything back
       }
       catch (UnknownHostException e) {
           throw e; 
       }
       catch (IOException e) {
           throw e; 
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
        try {
            client.initialConnect();        
//        client.startCLI();  
//        client.listen();    //listens for server to connect to it (server keeps cache of ip addresses)

            //test
            Topic topic = new Topic("1_topic_1");
            client.advertise(topic);

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


