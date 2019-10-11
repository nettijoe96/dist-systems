package pubsub;


import java.net.*;
import java.io.*;

public class ClientInit implements Runnable{

    static final String CONNECT = "connect";
    static final String PUBLISHER = "publisher";
    static final String SUBSCRIBER = "subscriber";

    private Socket socket;
    private BrokerListener listener;
    
    public ClientInit( Socket socket, BrokerListener listener ){
        this.socket = socket;
        this.listener = listener;
    }

    public void run(){
        try{
            // Get the first packet (Should be a connect packet)
            ObjectInputStream in = new ObjectInputStream( socket.getInputStream() );
            Packet packet = (Packet) in.readObject();
            if( packet.getPacketType().equals(CONNECT) ){ // Check that it's connect
                ConnectPacket connectPacket = (ConnectPacket) packet;
                // Get your connack after processing the connect packet
                ConnackPacket connackPacket = (ConnackPacket) listener.invoke( connectPacket );
                // Start the persistent client handler thread
                ClientHandler clientHandler = new ClientHandler( this.socket, this.listener, connackPacket );
                Thread clientHandlerThread = new Thread( clientHandler );
                clientHandlerThread.start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
 
    }
}

