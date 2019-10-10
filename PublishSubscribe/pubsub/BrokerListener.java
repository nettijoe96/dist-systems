package pubsub;


import java.util.concurrent.*;
import pubsub.packet.*;
import java.net.*;
import java.io.*;


public class BrokerListener implements Runnable{

    static final String CONNECT = "connect";
    static final String PUBLISHER = "publisher";
    static final String SUBSCRIBER = "subscriber";

    private int port;
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

        this.serverSocket = new ServerSocket( this.port );
        System.out.println("Server socket opened")

    }

    private void addClient( Socket socket ){
        ClientInit init = new ClientInit( socket, this );
        
    }

    private ConnackPacket invokeConnectSubscriber( ConnectPacket connectPacket ){
        this.subList.addSub( connectPacket );
        
        return new ConnackPacket( connectPacket );

    }

    private ConnackPacket invokeConnectPublisher( ConnectPacket connectPacket ){
        this.pubList.addPub( connectPacket );

        return new ConnackPacket( connectPacket );
    }

    private ConnackPacket invokeConnect( ConnectPacket connectPacket ){ 
        if( connectPacket.getConnectionType().equals( SUBSCRIBER ){
            return invokeConnectSubscriber( connectPacket );
        } else {
            return invokeConnectPublisher( connectPacket );
        }
    } 

    public Packet invoke( Packet packet ){
        System.out.println("Packet invoked");
        if( packet.getPacketType().equals( CONNECT ) ){
            return invokeConnect( (ConnectPacket) packet );
        }
        // Add methods for all types of packets
    }


    public void run(){
        Socket socket = new Socket

        while true{
            System.out.println("Waiting for client connection");

            socket = this.serverSocket.accept();

            System.out.println("New client connected!");

            addClient( socket );

        }
    }


}

