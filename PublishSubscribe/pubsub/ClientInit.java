package pubsub;


import java.net.*;
import java.io.*;

public class ClientInit implements Runnable{

    static final String CONNECT = "connect";

    private Socket socket;
    private BrokerListener listener;
    
    public ClientInit( Socket socket, BrokerListener listener ){
        this.socket = socket;
        this.listener = listener;
    }

    public void run(){
        ObjectInputSteam in = new ObjectInputStream( socket.getInputStream() );
        Packet packet = (Packet) in.readObject();
        if(packet.getType().equals(
    }
}

