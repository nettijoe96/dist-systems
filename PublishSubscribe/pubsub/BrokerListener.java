/*
 * The BrokerListener listens for nodes to connect to the network and handles adding them to memory...
 */


package pubsub;


import java.util.concurrent.*;
import pubsub.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.net.InetAddress;
import java.io.IOException;
import java.net.UnknownHostException;

public class BrokerListener implements Runnable{


    // Data structures that must be kept track of
    private Globals globals;
    private Broker broker;
    private ServerSocket serverSocket;    

    public BrokerListener(Broker broker) throws UnknownHostException, IOException {
        this.globals = new Globals();
        this.broker = broker;

        try{
//            this.serverSocket = new ServerSocket(this.globals.BROKER_PORT, this.globals.serverConnectionBacklog, InetAddress.getByName(this.globals.BROKER_IP)); //TODO: this ip might not be needed once we have docker cont on that address
            this.serverSocket = new ServerSocket(this.globals.BROKER_PORT);

        } catch( UnknownHostException e ){
            throw e;
        }
        catch( IOException e ){
            throw e;
        }

        System.out.println("Server socket opened");

    }

/*
     * Starts a service that will handle adding the client to either subList or pubList
     * Used to avoid waiting too long on one client to send connect packet
     * allows for multiple clients to connect in quick succession even if one misbehaves
*/
    private void addClient( Socket socket ){
        ClientInit init = new ClientInit( socket, this );
        Thread initThread = new Thread(init);
        initThread.start();
    }


    /*
     * Processes generic connect packets,
     */
    private ConnackPacket invokeConnect( ConnectPacket connectPacket ){ 
        if( connectPacket.getDeviceUUID() == this.globals.initDeviceUUID) {
            //here we have a brand new client. We need to give it a uuid.
            int clientUUID = this.broker.getNewUUID();
            return new ConnackPacket( connectPacket, clientUUID );
            
        }
        else {
            return new ConnackPacket( connectPacket, connectPacket.getDeviceUUID() );
            
        }

    } 

    /*
     * Generic packet handling function
     * Calls specific methods depending on packet type
     */
    public Packet invoke( Packet packet ){
        System.out.println("Packet invoked");
        // Connect Packet
        if( packet.getPacketType().equals( this.globals.CONNECT ) ){
            return invokeConnect( (ConnectPacket) packet );
        }

        // Add methods for all types of packets
        // TODO: Publish
        // TODO: Subscribe
        // TODO: Advertise
        // TODO: Notify
        
        // Shouldn't return a null packet? Always an ACK for QoS 1?
        return null;
    }


    public void run(){

        // Continually listens for new client connections and adds them
        while(true){
            System.out.println("Waiting for client connection");

            try{
                Socket socket;
                socket = this.serverSocket.accept();
                System.out.println("New client connected!");

                addClient( socket );
                System.out.println("after addClient");
                
            }catch( Exception e ){
                e.printStackTrace();
            }
        }
    }
}

