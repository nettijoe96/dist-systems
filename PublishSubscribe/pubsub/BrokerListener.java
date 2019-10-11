/*
 * The BrokerListener listens for nodes to connect to the network and handles adding them to memory...
 */


package pubsub;


import java.util.concurrent.*;
import pubsub.*;
import java.net.*;
import java.io.*;
import java.util.*;


public class BrokerListener implements Runnable{

    // Constants denoting certain types
    // TODO: Move to a constants file or something so that these are shared
    static final String CONNECT = "connect";
    static final String PUBLISHER = "publisher";
    static final String SUBSCRIBER = "subscriber";

    // TODO: Move port to a consts file and then this shouldn't be a variable
    private int port;
    // Data structures that must be kept track of
    private SubList subList;
    private PubList pubList;
    private ArrayList<Topic> topics = new ArrayList<>();
    private Semaphore topicsMutex = new Semaphore(1);
    private ArrayList<Event> events = new ArrayList<>();
    private Semaphore eventsMutex = new Semaphore(1);
    private ServerSocket serverSocket;


    public BrokerListener( int port ){
        this.subList = new SubList();
        this.pubList = new PubList();
        this.port = port;

        try{
            this.serverSocket = new ServerSocket( this.port );
        }catch( Exception e ){
            e.printStackTrace();
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
        init.start();
    }

    /*
     * Processes a connect packet that comes from a subscriber
     * Adds them to sublist (TODO: actually make SubList methods and stuff)
     * returns a connack packet
     * TODO: Send them any messages that they need (part of sublist.addSub?)
     */
    private ConnackPacket invokeConnectSubscriber( ConnectPacket connectPacket ){
        this.subList.addSub( connectPacket );
        
        return new ConnackPacket( connectPacket );

    }

    /*
     * Processes connect packet from publishers
     * TODO: actually implement PubList
     */
    private ConnackPacket invokeConnectPublisher( ConnectPacket connectPacket ){
        this.pubList.addPub( connectPacket );

        return new ConnackPacket( connectPacket );
    }

    /*
     * Processes generic connect packets,
     * calls sub or pub connect methods
     */
    private ConnackPacket invokeConnect( ConnectPacket connectPacket ){ 
        if( connectPacket.getConnectionType().equals( SUBSCRIBER ) ){
            return invokeConnectSubscriber( connectPacket );
        } else {
            return invokeConnectPublisher( connectPacket );
        }
    } 

    /*
     * Generic packet handling function
     * Calls specific methods depending on packet type
     */
    public Packet invoke( Packet packet ){
        System.out.println("Packet invoked");
        // Connect Packet
        if( packet.getPacketType().equals( CONNECT ) ){
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
            }catch( Exception e ){
                e.printStackTrace();
            }
        }
    }
}

