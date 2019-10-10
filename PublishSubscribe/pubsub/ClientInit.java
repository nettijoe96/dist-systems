package pubsub;


import pubsub.packet.*;
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
        ObjectInputSteam in = new ObjectInputStream( socket.getInputStream() );
        Packet packet = (Packet) in.readObject();
        if( packet.getPacketType().equals(CONNECT) ){
            ConnectPacket connectPacket = (ConnectPacket) packet;
            if( connectPacket.getConnectionType().equals( PUBLISHER ) ){
                ConnackPacket connackPacket = listener.invoke( connectPacket );
                SubHandler subHandler = new subHandler( this.socket, this.listener, connackPacket );
                subHandler.start();
            }else
        }
    }
}

