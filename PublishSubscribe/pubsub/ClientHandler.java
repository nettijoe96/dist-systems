package pubsub;

import java.net.*;
import java.io.*;

public class ClientHandler implements Runnable{
    private Socket socket;
    private BrokerListener listener;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler( Socket socket, BrokerListener listener, ConnackPacket connackPacket ){
        try{
            this.socket = socket;
            this.listener = listener;
            this.out = new ObjectOutputStream( socket.getOutputStream() );
            this.in = new ObjectInputStream( socket.getInputStream() );
            this.out.writeObject( connackPacket );
        }catch( Exception e ){
            e.printStackTrace();
        }
    }

    private Packet invokeBroker( Packet packet ){
        return this.listener.invoke( packet );
    }

    public void run(){
        while(true){
            try{
                Packet packet = (Packet) this.in.readObject();
                packet = invokeBroker( packet );
                this.out.writeObject( packet );    
            }catch( Exception e ){
                e.printStackTrace();
            }
        }
    }
}


