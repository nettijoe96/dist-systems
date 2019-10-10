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
        ObjectInputStream in = new ObjectInputStream( socket.getInputStream() );
        Packet packet = (Packet) in.readObject();
        if( packet.getPacketType().equals(CONNECT) ){
            ConnectPacket connectPacket = (ConnectPacket) packet;
            ConnackPacket connackPacket = listener.invoke( connectPacket );
            ClientHandler clientHandler = new ClientHandler( this.socket, this.listener, connackPacket );
            Thread clientHandlerThread = new Thread( clientHandler );
            clientHandlerThread.start();
        }
    }
}

